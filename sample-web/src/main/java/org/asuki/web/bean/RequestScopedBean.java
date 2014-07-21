package org.asuki.web.bean;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@RequestScoped
@NoArgsConstructor
public class RequestScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int count;

    public void countUp() {
        count++;
    }
}
