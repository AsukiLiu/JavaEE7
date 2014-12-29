package org.asuki.camel;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.apache.camel.cdi.CdiCamelContext;

public class CamelCdiVetoExtension implements Extension {
    void interceptProcessAnnotatedTypes(
            @Observes ProcessAnnotatedType processAnnotatedType) {

        if (processAnnotatedType.getAnnotatedType().getJavaClass().getName()
                .equals(CdiCamelContext.class.getName())) {
            processAnnotatedType.veto();
        }
    }
}
