package org.asuki.webservice.rs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BeanMixIn {

    @JsonProperty("coffeeName")
    public abstract String getName();

    @JsonIgnore
    public abstract String getPrice();

}