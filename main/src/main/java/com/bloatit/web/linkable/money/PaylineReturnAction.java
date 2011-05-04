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
package com.bloatit.web.linkable.money;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.actions.ElveosAction;
import com.bloatit.web.url.PaylineReturnActionUrl;

@ParamContainer(value = "payline/doreturn", protocol = Protocol.HTTPS)
public final class PaylineReturnAction extends ElveosAction {

    @RequestParam(name = "token")
    @Optional
    private final String token;

    @RequestParam(name = "ack")
    private final String ack;

    @RequestParam(name = "process")
    private final PaylineProcess process;

    public PaylineReturnAction(final PaylineReturnActionUrl url) {
        super(url);
        token = url.getToken();
        ack = url.getAck();
        process = url.getProcess();
    }

    @Override
    protected Url doProcess(AuthToken authToken) {
        if (ack.equals("ok")) {
            try {
                process.validatePayment(token);
            } catch (final UnauthorizedPrivateAccessException e) {
                session.notifyBad(Context.tr("Right error when trying to validate the payment: {0}", process.getPaymentReference(token)));
            }
        } else if (ack.equals("cancel")) {
            process.refusePayment(token);
        }
        final Url target = process.close();
        if (target != null) {
            return target;
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors(AuthToken authToken) {
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(AuthToken authToken) {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // No post parameters.
    }

}
