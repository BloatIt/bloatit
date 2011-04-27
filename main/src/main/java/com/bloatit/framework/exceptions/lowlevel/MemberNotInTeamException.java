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
package com.bloatit.framework.exceptions.lowlevel;

public class MemberNotInTeamException extends Exception {
    private static final long serialVersionUID = -857528512589863370L;

    public MemberNotInTeamException() {
        super();
    }

    public MemberNotInTeamException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MemberNotInTeamException(final String message) {
        super(message);
    }

    public MemberNotInTeamException(final Throwable cause) {
        super(cause);
    }
}
