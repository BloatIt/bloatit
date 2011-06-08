//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.invoice;

import java.util.HashSet;
import java.util.Set;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Actor;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.actions.WebProcess;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;

public class ContactBox extends HtmlDiv {

    private ContactBox(Actor<?> actor, WebProcess process) throws UnauthorizedPrivateAccessException {
        super("contact_box");

        add(new HtmlDefineParagraph(Context.tr("Invoicing name: "), actor.getContact().getName()));
        add(new HtmlDefineParagraph(Context.tr("Invoicing adress: "), actor.getContact().getAddress()));
        add(new ModifyInvoicingContactProcessUrl(actor, process).getHtmlLink(Context.tr("Modify invoicing contact")));
    }

    public static HtmlElement generate(Actor<?> actor, WebProcess process) {
        try {
            if (actor.hasInvoicingContact()) {
                return new ContactBox(actor, process);
            } else {
                return new PlaceHolderElement();
            }
        } catch (UnauthorizedPrivateAccessException e) {
            throw new ShallNotPassException("User cannot access user contact information", e);
        }
    }
}
