package org.asuki.web.service;

import javax.ejb.Local;

@Local
public interface NestedService {
    void doSomething();
}
