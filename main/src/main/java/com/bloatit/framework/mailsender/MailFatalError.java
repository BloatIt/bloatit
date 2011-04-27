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
package com.bloatit.framework.mailsender;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public class MailFatalError extends BadProgrammerException {
    private static final long serialVersionUID = -1660347313919720091L;

    protected MailFatalError(final String string, final Throwable cause) {
        super(string, cause);
    }

    public MailFatalError(final Throwable cause) {
        super(cause);
    }

    public MailFatalError(final String message) {
        super(message);
    }

}
