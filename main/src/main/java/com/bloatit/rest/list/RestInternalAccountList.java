package com.bloatit.rest.list;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.InternalAccount;
import com.bloatit.rest.list.master.RestListBinder;
import com.bloatit.rest.resources.RestInternalAccount;

/**
 * <p>
 * Wraps a list of InternalAccount into a list of RestElements
 * </p>
 * <p>
 * This class can be represented in Xml as a list of InternalAccount<br />
 * Example:
 * 
 * <pre>
 * {@code <InternalAccounts>}
 *     {@code <InternalAccount name=InternalAccount1 />}
 *     {@code <InternalAccount name=InternalAccount2 />}
 * {@code </InternalAccounts>}
 * </pre>
 * <p>
 */
@XmlRootElement
public class RestInternalAccountList extends RestListBinder<RestInternalAccount, InternalAccount> {
    /**
     * Creates a RestInternalAccountList from a
     * {@codePageIterable<InternalAccount>}
     * 
     * @param collection the list of elements from the model
     */
    public RestInternalAccountList(final PageIterable<InternalAccount> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElementWrapper(name = "internalaccounts")
    @XmlElement(name = "internalaccount")
    public RestInternalAccountList getInternalAccounts() {
        return this;
    }
}
