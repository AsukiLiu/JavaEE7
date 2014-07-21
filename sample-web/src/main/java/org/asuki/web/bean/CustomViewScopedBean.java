package org.asuki.web.bean;

import java.io.Serializable;

import javax.inject.Named;

import org.asuki.web.annotation.CustomViewScoped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@CustomViewScoped
@NoArgsConstructor
public class CustomViewScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int count;

    public void countUp() {
        count++;
    }
}
