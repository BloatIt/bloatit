/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.login;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.web.actions.ElveosAction;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.MemberActivationActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer(value = "member/doactivate", protocol = Protocol.HTTPS)
public final class MemberActivationAction extends ElveosAction {

    @RequestParam
    private final String login;

    @RequestParam(role = Role.GET)
    private final String key;

    // Keep it for consistency
    @SuppressWarnings("unused")
    private final MemberActivationActionUrl url;

    public MemberActivationAction(final MemberActivationActionUrl url) {
        super(url);
        this.url = url;

        this.login = url.getLogin();
        this.key = url.getKey();
    }

    @Override
    protected Url doProcess(final ElveosUserToken userToken) {
        final Member member = MemberManager.getMemberByLogin(login);

        final Url to = new IndexPageUrl();

        if (member != null) {
            if (member.getActivationState() == ActivationState.VALIDATING) {
                if (key.equals(member.getActivationKey())) {
                    member.activate(key);

                    // Auto login after activation
                    session.authenticate(new AuthenticatedUserToken(member));
                    session.notifyGood(Context.tr("Activation sucess, you are now logged."));

                } else {
                    session.notifyWarning(Context.tr("Wrong activation key for this member."));
                }
            } else {
                session.notifyWarning(Context.tr("No activation needed for this member."));
            }
        } else {
            session.notifyWarning(Context.tr("Activation impossible on a not existing member."));
        }

        return to;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final ElveosUserToken userToken) {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // No post parameter to transmit.
    }
}
