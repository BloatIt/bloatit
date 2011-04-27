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
package com.bloatit.web.linkable.errors;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.url.NotFoundActionUrl;

@ParamContainer("dopagenotfound")
public class NotFoundAction extends Action {

    public NotFoundAction(NotFoundActionUrl url) {
        super(url);
    }

    @Override
    protected Url doProcess() {
        Context.getLocalizator().forceLanguageReset();
        return new PageNotFoundUrl();
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        // Doesn't happen
        return null;
    }

    @Override
    protected void transmitParameters() {
        // No parameter
    }

}
