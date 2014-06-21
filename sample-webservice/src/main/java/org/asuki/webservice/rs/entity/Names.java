package org.asuki.webservice.rs.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.NoArgsConstructor;

@XmlRootElement(name = "names")
@NoArgsConstructor
public class Names {

    private List<String> names;

    public Names(List<String> names) {
        this.names = names;
    }

    @XmlAttribute(name = "count")
    public int getCount() {
        return names.size();
    }

    @XmlElement(name = "name")
    public List<String> getNames() {
        return names;
    }
}