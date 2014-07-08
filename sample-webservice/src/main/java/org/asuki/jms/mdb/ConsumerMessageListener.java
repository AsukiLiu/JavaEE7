package org.asuki.jms.mdb;

import static org.asuki.common.Constants.Messages.TEST_QUEUE;

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.asuki.common.dsl.fluent.Person;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = TEST_QUEUE),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class ConsumerMessageListener implements MessageListener {

    @Inject
    private Logger log;

    @Inject
    private ObjectMapper objectMapper;

    @Resource
    private MessageDrivenContext mdContext;

    @Override
    public void onMessage(Message message) {
        try {

            if (message instanceof ObjectMessage) {
                // JMS 1.1
                ObjectMessage objectMessage = (ObjectMessage) message;
                Person person11 = (Person) objectMessage.getObject();
                log.info("Message(JMS 1.1): " + person11);

                // JMS 2.0
                Person person20 = message.getBody(Person.class);
                log.info("Message(JMS 2.0): " + person20);
            }

            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();

                Person person = objectMapper.readValue(text,
                        new PersonTypeReference());
                log.info("Message(Jackson): " + person);
            }

        } catch (JMSException | IOException e) {
            mdContext.setRollbackOnly();

            log.error(e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    private static class PersonTypeReference extends TypeReference<Person> {
    }
}
