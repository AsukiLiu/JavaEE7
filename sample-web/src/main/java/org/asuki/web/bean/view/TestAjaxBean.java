package org.asuki.web.bean.view;

import java.util.Date;

import javax.enterprise.inject.Model;

@Model
public class TestAjaxBean {

    public String getNowTime() {
        return (new Date()).toString();
    }
}
