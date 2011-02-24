package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.ExternalAccount;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestExternalAccount;

/**
 * <p>
 * Wraps a list of ExternalAccount into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of ExternalAccount<br />
 * Example: 
 * 
 * <pre>
 * {@code <ExternalAccounts>}
 *     {@code <ExternalAccount name=ExternalAccount1 />}
 *     {@code <ExternalAccount name=ExternalAccount2 />}
 * {@code </ExternalAccounts>}
 * </pre>
 * <p>
 */ 
@XmlRootElement
public class RestExternalAccountList extends RestListBinder<RestExternalAccount, ExternalAccount> {
    /**
     * Creates a RestExternalAccountList from a {@codePageIterable<ExternalAccount>}
     *
     * @param collection the list of elements from the model
     */
    public RestExternalAccountList(PageIterable<ExternalAccount> collection) {
        super(collection);
    }
    
    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "externalaccounts")
    @XmlElement(name = "externalaccount")
    public RestExternalAccountList getExternalAccounts() {
        return this;
    }
}

