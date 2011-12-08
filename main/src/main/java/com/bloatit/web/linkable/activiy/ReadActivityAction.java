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
package com.bloatit.web.linkable.activiy;

import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.ReadActiviyActionUrl;

/**
 * A response to a form used to assess any <code>kudosable</code> on the bloatit
 * website
 */
@ParamContainer("activiy/read")
public final class ReadActiviyAction extends LoggedElveosAction {


    public ReadActiviyAction(final ReadActiviyActionUrl url) {
        super(url);
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        me.setLastWatchedEvents(DateUtils.now());
        return session.pickPreferredPage();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return "You must be logged to set your activiy as read";
    }

    @Override
    protected void transmitParameters() {
        // Nothing to save
    }

}
