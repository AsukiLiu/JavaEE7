package org.asuki.web.bean;

import static java.lang.System.currentTimeMillis;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;

import lombok.Getter;

import org.slf4j.Logger;

@TransactionScoped
public class TransactionScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Getter
    private long value;

    @PostConstruct
    private void init() {
        value = currentTimeMillis();
        log.info("initialized [{}]", value);
    }

    @PreDestroy
    private void destroy() {
        log.info("destroyed [{}]", value);
    }

}
