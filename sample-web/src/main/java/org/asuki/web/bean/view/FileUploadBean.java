package org.asuki.web.bean.view;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;

import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;

@Model
public class FileUploadBean {

    @Inject
    private Logger log;

    @Setter
    @Getter
    private Part file;

    public void upload() {
        if (file == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Select a file"));
            return;
        }

        log.info("Content type:{}", file.getContentType());
        log.info("Submitted filename:{}", file.getSubmittedFileName());
        log.info("Size:{}", file.getSize());

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Uploaded successfully"));
    }

}
