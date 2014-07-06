package org.asuki.webservice.rs.param;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CustomParamImpl implements CustomParam {

    @Getter
    private String value;

}
