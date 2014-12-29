package org.asuki.camel;

import javax.inject.Named;
import org.apache.camel.Body;

@Named
public class CustomBean {

    public static final String REF_CLASS_NAME = "customBean";
    public static final String REF_METHOD_NAME = "doSomething";

    public String doSomething(@Body String message) {
        return "Sent " + message;
    }
}
