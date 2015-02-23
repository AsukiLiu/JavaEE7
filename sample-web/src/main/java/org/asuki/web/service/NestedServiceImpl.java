package org.asuki.web.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.web.bean.TransactionScopedBean;
import org.slf4j.Logger;

@Stateless
public class NestedServiceImpl implements NestedService {

    @Inject
    private Logger log;

    @Inject
    private TransactionScopedBean transactionScopedBean;

    @Override
    public void doSomething() {
        log.info("transactionScopedBean [{}]", transactionScopedBean.getValue());
    }

}
