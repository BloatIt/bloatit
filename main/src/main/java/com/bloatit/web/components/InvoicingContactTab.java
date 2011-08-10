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
package com.bloatit.web.components;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Contact;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.actions.RedirectWebProcess;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.team.TeamPage;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;
import com.bloatit.web.url.TeamPageUrl;

public class InvoicingContactTab extends HtmlTab {
    private final Actor<?> actor;

    public InvoicingContactTab(final Actor<?> actor, final String title, final String tabKey) {
        super(title, tabKey);
        this.actor = actor;
    }

    @Override
    public XmlNode generateBody() {
        final HtmlDiv master = new HtmlDiv("tab_pane");

        final HtmlDiv modify = new HtmlDiv("float_right");
        master.add(modify);
        Url redirectUrl;
        if (actor.isTeam()) {
            final TeamPageUrl teampPageUrl = new TeamPageUrl((Team) actor);
            teampPageUrl.setActiveTabKey(TeamPage.INVOICING_TAB);
            redirectUrl = teampPageUrl;
        } else {
            final MemberPageUrl memberPageUrl = new MemberPageUrl((Member) actor);
            memberPageUrl.setActiveTabKey(MemberPage.INVOICING_TAB);
            redirectUrl = memberPageUrl;
        }

        Contact contact;
        boolean all;
        try {
            contact = actor.getContact();
            all = actor.hasInvoicingContact(true);
        } catch (final UnauthorizedPrivateAccessException e) {
            throw new ShallNotPassException(e);
        }

        ModifyInvoicingContactProcessUrl modifyInvoicingContactProcessUrl = new ModifyInvoicingContactProcessUrl(actor,
                                                                                                                 new RedirectWebProcess(redirectUrl));
        modifyInvoicingContactProcessUrl.setNeedAllInfos(all);
        modify.add(modifyInvoicingContactProcessUrl.getHtmlLink(Context.tr("Change invoicing informations")));

        final HtmlList memberIdList = new HtmlList();
        master.add(memberIdList);

        // Name
        final String name;
        if (actor.isTeam()) {
            name = Context.tr("Organisation name: ");
        } else {
            name = Context.tr("Name: ");
        }
        memberIdList.add(new HtmlDefineParagraph(name, emptyIfNull(contact.getName())));

        // Street
        final String street;
        street = Context.tr("Street: ");
        memberIdList.add(new HtmlDefineParagraph(street, emptyIfNull(contact.getStreet())));

        // Extras
        final String extras;
        extras = Context.tr("Extras adress: ");
        memberIdList.add(new HtmlDefineParagraph(extras, emptyIfNull(contact.getExtras())));

        // City
        final String city;
        city = Context.tr("City: ");
        memberIdList.add(new HtmlDefineParagraph(city, emptyIfNull(contact.getCity())));

        // Postal code
        final String postalCode;
        postalCode = Context.tr("Postal code: ");
        memberIdList.add(new HtmlDefineParagraph(postalCode, emptyIfNull(contact.getPostalCode())));

        // Country
        final String country;
        country = Context.tr("Country: ");
        memberIdList.add(new HtmlDefineParagraph(country, emptyIfNull(contact.getCountry())));

        if (all) {

            memberIdList.add(new HtmlTitle(Context.tr("Invoice emission informations"), 2));

            // Invoicing id template
            final String invoicingIdTemplate;
            invoicingIdTemplate = Context.tr("Invoicing id template: ");
            memberIdList.add(new HtmlDefineParagraph(invoicingIdTemplate, emptyIfNull(contact.getInvoiceIdTemplate())));

            // Invoicing id number
            final String invoicingIdNumber;
            invoicingIdNumber = Context.tr("Invoicing id next number: ");
            if (contact.getInvoiceIdNumber() != null) {
                memberIdList.add(new HtmlDefineParagraph(invoicingIdNumber, contact.getInvoiceIdNumber().toPlainString()));
            } else {
                memberIdList.add(new HtmlDefineParagraph(invoicingIdNumber, ""));
            }

            // Legal Id
            final String legalId;
            legalId = Context.tr("Legal identification: ");
            memberIdList.add(new HtmlDefineParagraph(legalId, emptyIfNull(contact.getLegalId())));

            // Tax identification
            final String taxIdentification;
            taxIdentification = Context.tr("Tax identification: ");
            memberIdList.add(new HtmlDefineParagraph(taxIdentification, emptyIfNull(contact.getTaxIdentification())));

            // Tax rate
            final String taxRate;
            taxRate = Context.tr("Tax rate: ");
            if (contact.getTaxRate() != null) {
                memberIdList.add(new HtmlDefineParagraph(taxRate, contact.getTaxRate().multiply(new BigDecimal("100")).toPlainString() + " %"));
            } else {
                memberIdList.add(new HtmlDefineParagraph(taxRate, ""));
            }

        }

        return master;
    }

    private String emptyIfNull(final String input) {
        if (input == null) {
            return "";
        }
        return input;
    }

}
