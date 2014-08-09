package org.asuki.model.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VetoExtension implements Extension {

    private Logger log = LoggerFactory.getLogger(getClass());

    public void vetoEntities(@Observes ProcessAnnotatedType<?> pat) {
        if (pat.getAnnotatedType().getAnnotation(Entity.class) != null) {
            pat.veto();
            log.info("Vetoed class: " + pat.getAnnotatedType().getJavaClass());
        }
    }
}
