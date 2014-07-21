package org.asuki.web.cdi;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.asuki.web.annotation.CustomViewScoped;

public class CustomViewContext implements Context {

    @Override
    public Class<? extends Annotation> getScope() {
        return CustomViewScoped.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Contextual<T> contextual,
            CreationalContext<T> creationalContext) {

        Bean<T> bean = (Bean<T>) contextual;
        Map<String, Object> viewMap = getViewMap();

        String key = bean.getName();

        if (viewMap.containsKey(key)) {
            return (T) viewMap.get(key);
        }

        if (creationalContext == null) {
            return null;
        }

        T t = bean.create(creationalContext);
        viewMap.put(key, t);
        return t;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        return get(contextual, null);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    private Map<String, Object> getViewMap() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = fctx.getViewRoot();
        return viewRoot.getViewMap(true);
    }
}
