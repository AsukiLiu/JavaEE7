package org.asuki.web.bean;

import java.io.Serializable;

import javax.inject.Named;
import javax.faces.view.ViewScoped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@ViewScoped
@NoArgsConstructor
public class ViewScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int count;

    public void countUp() {
        count++;
    }
}