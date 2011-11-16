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
package com.bloatit.web.linkable.members;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;
import com.bloatit.web.url.ModifyNewsletterActionUrl;

@ParamContainer(value = "member/domodifynewsletter", protocol = Protocol.HTTPS)
public class ModifyNewsletterAction extends LoggedElveosAction {

    @RequestParam(name = "bloatit_newsletter", role = Role.POST)
    private final Boolean newsletter;

    private final ModifyNewsletterActionUrl url;

    public ModifyNewsletterAction(final ModifyNewsletterActionUrl url) {
        super(url);
        Boolean newsletterTmp = url.getNewsletter();
        if (newsletterTmp == null) {
            this.newsletter = Boolean.FALSE;
        } else {
            this.newsletter = newsletterTmp;
        }
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        me.acceptNewsLetter(newsletter);
        if (newsletter.booleanValue()) {
            session.notifyGood(Context.tr("Thank you for registering to Elveos newsletter. Don't worry we'll not spam !"));
        } else {
            session.notifyGood(Context.tr("You will no longer receive the newsletter. If you feel it was not interesting or too psmamy, feel free to indicate us using the suggestion form !"));
        }
        return new MemberPageUrl(me);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyMemberPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify your account settings.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getNewsletterParameter());
    }
}
