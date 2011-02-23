package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Translation;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestTranslationList;

@XmlRootElement
public class RestTranslation extends RestElement<Translation> {
    private final Translation model;

    protected RestTranslation(final Translation model) {
        this.model = model;
    }

    @REST(name = "translations", method = RequestMethod.GET)
    public static RestTranslation getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "translations", method = RequestMethod.GET)
    public static RestTranslationList getAll() {
        // TODO auto generated code
        return null;
    }

    Translation getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
