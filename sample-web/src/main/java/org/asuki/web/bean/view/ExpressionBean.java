package org.asuki.web.bean.view;

import javax.annotation.PostConstruct;
import javax.el.ELProcessor;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import lombok.Getter;

@Named
@RequestScoped
public class ExpressionBean {

    @Getter
    private Object expression;

    @PostConstruct
    public void init() {
        ELProcessor elProcessor = new ELProcessor();

        expression = elProcessor.eval("[1,2,3,4].stream().sum()");
    }
}