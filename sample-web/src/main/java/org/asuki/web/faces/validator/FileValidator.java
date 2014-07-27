package org.asuki.web.faces.validator;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

@FacesValidator
public class FileValidator implements Validator {

    private static final int MAX_FILE_SIZE = 1024;

    @Override
    public void validate(FacesContext context, UIComponent component,
            Object value) throws ValidatorException {

        if (value == null) {
            return;
        }

        Part part = (Part) value;
        if (part.getSize() > MAX_FILE_SIZE) {
            throw new ValidatorException(new FacesMessage(SEVERITY_ERROR,
                    "The file is too large",
                    "The size of the file is too large"));
        }
    }

}
