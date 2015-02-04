package org.asuki.camel;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.asuki.camel.dto.Input;
import org.asuki.camel.dto.Output;
import org.slf4j.Logger;

//http://localhost:8080/sample-web/rs/camel
@Path("camel")
@Singleton
//@Startup
@Lock(LockType.READ)
public class CamelRestService {

    public static final String URI = "direct:input";

    private RouteBuilder buildByFluent = new RouteBuilder() {
        @Override
        public void configure() {
            // @formatter:off
            from(URI)
                .routeId("cdi-route")
                .convertBodyTo(Input.class)
                .choice()
                    .when(simple("${body.inData} == 'A'"))
                        .setBody(constant(1))
                    .when(simple("${body.inData} == 'B'"))
                        .setBody(constant(2))
                    .otherwise()
                        .setBody(constant(-1));
            // @formatter:on
        }
    };

    private RouteBuilder buildByAnnotation = new RouteBuilder() {
        @Override
        public void configure() {
            // @formatter:off
            from(URI)
                .bean(PojoBean.class);
            // @formatter:on
        }
    };

    @Inject
    private CamelContext context;

    private CamelDelegate camelDelegate;

    @Inject
    private Logger log;

    @PostConstruct
    void init() throws Exception {
        camelDelegate = new ProxyBuilder(context).endpoint(URI).build(
                CamelDelegate.class);

        log.info(">> Creating CamelContext");

        try {
            context.addRoutes(buildByFluent);
            // context.addRoutes(buildByAnnotation);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }

        context.start();

        log.info(">> Created CamelContext");
    }

    // {"inData":"A"} → {"outData":1}
    @POST
    @Path("convert")
    @Consumes({ APPLICATION_JSON })
    @Produces({ APPLICATION_JSON })
    public Output convert(Input request) {
        return new Output(camelDelegate.doSomething(request));
    }

    private static interface CamelDelegate {
        int doSomething(Input request);
    }
}
