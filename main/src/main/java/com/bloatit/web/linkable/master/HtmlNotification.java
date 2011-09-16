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
package com.bloatit.web.linkable.master;

import com.bloatit.framework.webprocessor.ErrorMessage.Level;
import com.bloatit.framework.webprocessor.components.HtmlDiv;

public final class HtmlNotification extends HtmlDiv {

    public HtmlNotification(final Level level) {
        super();
        switch (level) {
            case INFO:
                setCssClass("notification_good");
                break;
            case WARNING:
                setCssClass("notification_bad");
                break;
            case FATAL:
                setCssClass("notification_error");
                break;
            default:
                // Do nothing
                break;
        }
    }

}
