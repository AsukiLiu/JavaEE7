package org.asuki.web.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.web.bean.TransactionScopedBean;
import org.slf4j.Logger;

@Stateless
public class MainServiceImpl implements MainService {

    @Inject
    private Logger log;

    @EJB
    private NestedService nestedService;

    @Inject
    private TransactionScopedBean transactionScopedBean;

    @Override
    public void doSomething() {
        log.info("transactionScopedBean [{}]", transactionScopedBean.getValue());

        nestedService.doSomething();
    }

}
