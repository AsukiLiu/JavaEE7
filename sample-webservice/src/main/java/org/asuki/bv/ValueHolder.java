package org.asuki.bv;

import lombok.Getter;
import lombok.Setter;

public class ValueHolder {

    @Getter
    @Setter
    @ForbiddenValues({ "password", "1234" })
    private String value;
}
