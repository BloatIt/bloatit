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
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedPrivateAccessException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.PaylineNotifyActionUrl;

@ParamContainer(value="payline/donotify", protocol=Protocol.HTTPS)
public final class PaylineNotifyAction extends Action {
    public static final String TOKEN_CODE = "token";

    @RequestParam(name = TOKEN_CODE)
    @Optional
    private final String token;

    @RequestParam(name = "process")
    private final PaylineProcess process;

    public PaylineNotifyAction(final PaylineNotifyActionUrl url) {
        super(url);
        token = url.getToken();
        process = url.getProcess();
    }

    @Override
    public Url doProcess() {
        Log.web().info("Get a payline notification: " + token);
        
        try {
            process.validatePayment(token);
        } catch (final UnauthorizedPrivateAccessException e) {
            throw new BadProgrammerException(e);
        }
        
        final Url target = process.close();
        if (target != null) {
            return target;
        }

        return new IndexPageUrl();
    }

    @Override
    public Url doProcessErrors() {
        Log.web().error("Payline notification with parameter errors ! ");
        return new IndexPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR; // Nothing else to check
    }

    @Override
    protected void transmitParameters() {
        // Nothing
    }
}
