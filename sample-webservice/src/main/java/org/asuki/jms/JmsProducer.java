package org.asuki.jms;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static org.asuki.common.Constants.Messages.*;
import static org.asuki.common.dsl.fluent.Person.with;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.asuki.common.dsl.fluent.Person;
import org.asuki.common.util.Function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("producer")
@Stateless
public class JmsProducer {

    @Resource(lookup = TEST_CONNECTION)
    private ConnectionFactory connectionFactory;

    @Resource(lookup = TEST_QUEUE)
    private Queue queue;

    @Inject
    @JMSConnectionFactory(TEST_CONNECTION)
    private JMSContext jmsContext;

    @Inject
    private ObjectMapper objectMapper;

    @Path("jms11/obj")
    @GET
    public String jms11Produce() {

        Person person = createPerson();

        Function<Session, Message, JMSException> createMessage = session -> {
            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(person);

            return objectMessage;
        };

        return sendMessage(createMessage);
    }

    @Path("jms11/json")
    @GET
    public String jms11ProduceByJackson() throws JsonProcessingException {

        Person person = createPerson();
        String text = objectMapper.writeValueAsString(person);

        Function<Session, Message, JMSException> createMessage = session -> session
                .createTextMessage(text);

        return sendMessage(createMessage);
    }

    @Path("jms20")
    @GET
    public String jms20Produce() {
        Person person = createPerson();

        jmsContext.createProducer().send(queue, person);

        return "Done";
    }

    private String sendMessage(
            Function<Session, Message, JMSException> createMessage) {
        String status = "Done";

        try (Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false,
                        AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(queue);) {

            Message message = createMessage.apply(session);

            producer.send(message);

        } catch (JMSException e) {
            status = e.getMessage();
        }

        return status;
    }

    private Person createPerson() {
        return with().firstName("Asuki").lastName("Liu").create();
    }
}
