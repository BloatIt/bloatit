package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.InternalAccount;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.list.RestInternalAccountList;

@XmlRootElement
public class RestInternalAccount extends RestElement<InternalAccount> {
    private final InternalAccount model;

    protected RestInternalAccount(final InternalAccount model) {
        this.model = model;
    }

    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestInternalAccount getById(final int id) {
        // TODO auto generated code
        return null;
    }

    @REST(name = "internalaccounts", method = RequestMethod.GET)
    public static RestInternalAccountList getAll() {
        // TODO auto generated code
        return null;
    }

    InternalAccount getModel() {
        return model;
    }

    @XmlAttribute
    @XmlID
    public String getId() {
        return model.getId().toString();
    }

}
