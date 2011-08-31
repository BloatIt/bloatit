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

import com.bloatit.common.Log;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.ElveosAction;
import com.bloatit.web.url.PaymentResponseActionUrl;

@ParamContainer(value = "payment/doresponse", protocol = Protocol.HTTPS)
public final class PaymentResponseAction extends ElveosAction {

    @RequestParam(name = "token")
    @Optional
    private final String token;

    @RequestParam(name = "process")
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final PaymentProcess process;

    @RequestParam(role = Role.POST, name = "DATA")
    @NonOptional(@tr("The bank data is missing."))
    private final String data;

    public PaymentResponseAction(final PaymentResponseActionUrl url) {
        super(url);
        token = url.getToken();
        process = url.getProcess();
        data = url.getData();
    }

    @Override
    protected Url doProcess() {
        try {
            process.handlePayment(data);
        } catch (final UnauthorizedOperationException e) {
            Log.web().error("Fail to validate payment", e);
            session.notifyWarning(Context.tr("Right error when trying to validate the payment: {0}", process.getPaymentReference()));
        }
        final Url target = process.close();
        if (target != null) {
            return target;
        }
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // No post parameters.
    }
}
