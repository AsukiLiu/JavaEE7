package org.asuki.webservice.rs.jackson;

import org.asuki.webservice.rs.entity.Bean;
import org.asuki.webservice.rs.entity.BeanMixIn;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomSimpleModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public CustomSimpleModule() {
        super("CustomSimpleModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.setMixInAnnotations(Bean.class, BeanMixIn.class);
    }
}
