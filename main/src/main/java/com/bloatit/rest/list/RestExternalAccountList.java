package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.ExternalAccount;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestExternalAccount;

@XmlRootElement
public class RestExternalAccountList extends RestListBinder<RestExternalAccount, ExternalAccount> {
    public RestExternalAccountList(PageIterable<ExternalAccount> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "externalaccounts")
    @XmlElement(name = "externalaccount")
    public RestExternalAccountList getExternalAccounts() {
        return this;
    }
}

