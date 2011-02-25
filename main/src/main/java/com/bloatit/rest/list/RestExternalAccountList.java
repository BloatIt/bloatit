/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.rest.list;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlIDREF;

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
@XmlRootElement(name = "externalaccounts")
public class RestExternalAccountList extends RestListBinder<RestExternalAccount, ExternalAccount> {

    /**
     * Provided for XML generation
     */
    @SuppressWarnings("unused")
    private RestExternalAccountList() {
        super();
    }

    /**
     * Creates a RestExternalAccountList from a
     * {@codePageIterable<ExternalAccount>}
     *
     * @param collection the list of elements from the model
     */
    public RestExternalAccountList(PageIterable<ExternalAccount> collection) {
        super(collection);
    }

    /**
     * This method is provided only to be able to represent the list as XmL
     */
    @XmlElement(name = "externalaccount")
    @XmlIDREF
    public List<RestExternalAccount> getExternalAccounts() {
        List<RestExternalAccount> externalaccounts = new ArrayList<RestExternalAccount>();
        for (RestExternalAccount externalaccount : this) {
            externalaccounts.add(externalaccount);
        }
        return externalaccounts;
    }
}
