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
package com.bloatit.model.right;

import java.util.EnumSet;

import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.model.Rights;

/**
 * The Class RgtBug store the properties accessor for the
 * {@link com.bloatit.model.Bug} class.
 */
public class RgtBug extends RightManager {

    /**
     * The Class <code>ErrorLevel</code> is a {@link RightManager.Public}
     * accessor for the <code>ErrorLevel</code> property.
     */
    public static class ErrorLevel extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class <code>Status</code> is a {@link RightManager.Public} accessor
     * for the <code>Status</code> property.
     */
    public static class Status extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class <code>Comment</code> is a {@link RightManager.Accessor}
     * accessor for the <code>Comment</code> property.
     */
    public static class Comment extends Accessor {

        @Override
        protected boolean can(final Rights object, final Action action) {
            return canRead(action) || authentifiedCanWrite(object, action);
        }
        
        @Override
        protected boolean authorizeWeakAccess(EnumSet<RightLevel> rights, final Action action) {
            return canRead(action) || rights.contains(RightLevel.COMMENT);
        }
    }

    /**
     * The Class <code>Description</code> is a {@link RightManager.Public}
     * accessor for the <code>Description</code> property.
     */
    public static class Description extends Public {
        // nothing this is just a rename.
    }
}
