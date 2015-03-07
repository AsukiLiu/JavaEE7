package org.asuki.webservice.rs.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.ws.rs.core.CacheControl;

@ApplicationScoped
public class CacheControlFactory {

    @Produces
    public CacheControl produce(InjectionPoint ip) {

        CacheControlConfig ccConfig = ip.getAnnotated().getAnnotation(
                CacheControlConfig.class);

        if (ccConfig == null) {
            return null;
        }

        CacheControl cc = new CacheControl();

        cc.setPrivate(ccConfig.isPrivate());
        cc.setNoCache(ccConfig.noCache());
        cc.setNoStore(ccConfig.noStore());
        cc.setNoTransform(ccConfig.noTransform());
        cc.setMustRevalidate(ccConfig.mustRevalidate());
        cc.setProxyRevalidate(ccConfig.proxyRevalidate());
        cc.setMaxAge(ccConfig.maxAge());
        cc.setSMaxAge(ccConfig.sMaxAge());

        return cc;
    }
}
