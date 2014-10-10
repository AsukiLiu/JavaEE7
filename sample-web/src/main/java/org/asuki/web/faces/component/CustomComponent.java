package org.asuki.web.faces.component;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

//In JSF 2.2, the XML file declaring tags is not required
//@FacesComponent(createTag=true)
@FacesComponent(value = "myComponent")
public class CustomComponent extends UIComponentBase {

    private Map<String, String> map = new HashMap<>();

    public CustomComponent() {
        map.put("key1", "item1");
        map.put("key2", "item2");
    }

    @Override
    public String getFamily() {
        return "customComponent";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

        String value = (String) getAttributes().get("value");

        if (isNullOrEmpty(value)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        String item = map.get(value);

        writer.write(item == null ? "Not found!" : item);
    }

}