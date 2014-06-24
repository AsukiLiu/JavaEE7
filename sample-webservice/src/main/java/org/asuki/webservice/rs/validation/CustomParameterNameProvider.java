package org.asuki.webservice.rs.validation;

import static java.util.Arrays.asList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;

public class CustomParameterNameProvider implements ParameterNameProvider {

    private final ParameterNameProvider nameProvider;

    public CustomParameterNameProvider() {
        nameProvider = Validation.byDefaultProvider().configure()
                .getDefaultParameterNameProvider();
    }

    @Override
    public List<String> getParameterNames(final Constructor<?> constructor) {
        return nameProvider.getParameterNames(constructor);
    }

    @Override
    public List<String> getParameterNames(final Method method) {

        switch (method.getName()) {
        case "getPerson":
            return asList("id");
        case "createPerson":
            return asList("id", "name");
        default:
            break;
        }

        return nameProvider.getParameterNames(method);
    }
}