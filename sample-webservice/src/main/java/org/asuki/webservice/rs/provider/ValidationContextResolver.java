package org.asuki.webservice.rs.provider;

import javax.validation.BootstrapConfiguration;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.validation.CustomParameterNameProvider;
import org.asuki.webservice.rs.validation.CustomMessageInterpolator;

import org.jboss.resteasy.plugins.validation.GeneralValidatorImpl;
import org.jboss.resteasy.spi.validation.GeneralValidator;

@Provider
public class ValidationContextResolver implements
        ContextResolver<GeneralValidator> {

    @Override
    public GeneralValidator getContext(Class<?> type) {

        Configuration<?> config = Validation.byDefaultProvider().configure();
        BootstrapConfiguration bootstrapConfiguration = config
                .getBootstrapConfiguration();

        // Interpolates a given constraint violation message
        config.messageInterpolator(new CustomMessageInterpolator(config
                .getDefaultMessageInterpolator()));

        // Determines if a property can be accessed by the Bean Validation
        // provider
        config.traversableResolver(config.getDefaultTraversableResolver());

        // Instantiates a ConstraintValidator instance based off its class
        config.constraintValidatorFactory(config
                .getDefaultConstraintValidatorFactory());

        // Provides names for method and constructor parameters
        config.parameterNameProvider(new CustomParameterNameProvider());

        return new GeneralValidatorImpl(config.buildValidatorFactory(),
                bootstrapConfiguration.isExecutableValidationEnabled(),
                bootstrapConfiguration.getDefaultValidatedExecutableTypes());
    }

}
