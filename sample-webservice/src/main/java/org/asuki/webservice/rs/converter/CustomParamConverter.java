package org.asuki.webservice.rs.converter;

import javax.ws.rs.ext.ParamConverter;

import org.asuki.webservice.rs.param.CustomParam;
import org.asuki.webservice.rs.param.CustomParamImpl;

public class CustomParamConverter implements ParamConverter<CustomParam> {

    @Override
    public CustomParam fromString(String value) {
        return new CustomParamImpl(value);
    }

    @Override
    public String toString(CustomParam param) {
        return param.getValue();
    }
}
