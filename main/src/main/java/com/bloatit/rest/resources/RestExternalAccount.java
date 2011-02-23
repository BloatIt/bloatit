package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.ExternalAccount;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestExternalAccountList;

@XmlRootElement
public class RestExternalAccount extends RestElement<ExternalAccount> {
    private final ExternalAccount model;

    protected RestExternalAccount(final ExternalAccount model) {
        this.model = model;
    }

    @REST(name = "externalaccounts", method = RequestMethod.GET)
    public static RestExternalAccount getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "externalaccounts", method = RequestMethod.GET)
    public static RestExternalAccountList getAll() {
        // TODO auto generated code
        return null;
    }

    ExternalAccount getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
