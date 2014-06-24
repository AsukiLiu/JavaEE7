package org.asuki.webservice.rs.validation;

import java.util.Locale;
import java.util.logging.Logger;

import javax.validation.MessageInterpolator;

import org.asuki.webservice.rs.util.ThreadLocalUtil;

public class CustomMessageInterpolator implements MessageInterpolator {

    private static final Logger LOG = Logger
            .getLogger(CustomMessageInterpolator.class.getName());

    private final MessageInterpolator defaultInterpolator;

    public CustomMessageInterpolator(MessageInterpolator interpolator) {
        this.defaultInterpolator = interpolator;
    }

    @Override
    public String interpolate(String message, Context context) {
        LOG.info("Selected language via thread-local: "
                + ThreadLocalUtil.get());
        return defaultInterpolator.interpolate(message, context,
                ThreadLocalUtil.get());
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        LOG.info("Selected language via locale: " + locale);
        return defaultInterpolator.interpolate(message, context, locale);
    }
}
