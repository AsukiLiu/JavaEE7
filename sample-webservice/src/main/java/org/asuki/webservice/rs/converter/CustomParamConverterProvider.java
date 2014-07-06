package org.asuki.webservice.rs.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.param.CustomParam;

@Provider
public class CustomParamConverterProvider implements ParamConverterProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
            Type genericType, Annotation[] annotations) {

        if (CustomParam.class.isAssignableFrom(rawType)) {
            return (ParamConverter<T>) new CustomParamConverter();
        }

        return null;
    }
}
