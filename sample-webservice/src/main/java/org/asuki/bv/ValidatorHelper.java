package org.asuki.bv;

import static java.util.Arrays.asList;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidatorHelper {
    public boolean isAllowed(String value, String[] forbiddenValues) {
        return !asList(forbiddenValues).contains(value);
    }
}
