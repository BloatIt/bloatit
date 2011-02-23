package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.InternalAccount;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestInternalAccount;

@XmlRootElement
public class RestInternalAccountList extends RestListBinder<RestInternalAccount, InternalAccount> {
    public RestInternalAccountList(PageIterable<InternalAccount> collection) {
        super(collection);
    }
    
    @XmlElementWrapper(name = "internalaccounts")
    @XmlElement(name = "internalaccount")
    public RestInternalAccountList getInternalAccounts() {
        return this;
    }
}

