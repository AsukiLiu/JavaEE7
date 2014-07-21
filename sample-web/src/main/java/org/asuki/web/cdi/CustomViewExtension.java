package org.asuki.web.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class CustomViewExtension implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery event,
            BeanManager manager) {
        event.addContext(new CustomViewContext());
    }
}