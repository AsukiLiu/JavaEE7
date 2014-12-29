package org.asuki.camel;

import static org.asuki.camel.CustomBean.REF_CLASS_NAME;
import static org.asuki.camel.CustomBean.REF_METHOD_NAME;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.CdiCamelContext;
import org.slf4j.Logger;

@Singleton
@Startup
public class Bootstrap {

    private static final String URI = "timer://timer1?period=1000";

    @Inject
    private CdiCamelContext context;

    @Inject
    private Logger log;

    @PostConstruct
    public void init() {
        log.info(">> Creating CamelContext");

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    // @formatter:off
                    from(URI)
                        .setBody()
                        .simple("message...")
                        .beanRef(REF_CLASS_NAME, REF_METHOD_NAME)
                        .log(">> Response: ${body}");
                    // @formatter:on
                }
            });
        } catch (Exception e) {
            log.error("", e);
            return;
        }

        context.start();

        log.info(">> Created CamelContext");
    }

    @PreDestroy
    public void shutdown() {
        context.stop();
        log.info(">> Stopped CamelContext");
    }

}
