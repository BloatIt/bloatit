package com.bloatit.framework.webprocessor.context;

import java.util.Timer;
import java.util.TimerTask;

import com.bloatit.framework.utils.datetime.DateUtils;

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

public class SessionCleanerTask extends TimerTask {

    private static final Timer cleanTimer = new Timer();

    public SessionCleanerTask() {
        cleanTimer.scheduleAtFixedRate(this, DateUtils.nowPlusSomeDays(1), DateUtils.SECOND_PER_DAY * DateUtils.MILLISECOND_PER_SECOND);
    }

    @Override
    public void run() {
        SessionManager.clearExpiredSessions();
    }

}
