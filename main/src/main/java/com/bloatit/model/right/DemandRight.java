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

import com.bloatit.model.Restricted;

public class DemandRight extends RightManager {

    public static class Description extends Accessor {
        @Override
        protected final boolean can(final Restricted role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class Offer extends Accessor {
        @Override
        protected final boolean can(final Restricted role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class Specification extends Accessor {
        @Override
        protected final boolean can(final Restricted role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class Contribute extends Accessor {
        @Override
        protected final boolean can(final Restricted role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class DemandContent extends Public {
        // nothing this is just a rename.
    }

    public static class Comment extends Accessor {
        @Override
        protected final boolean can(final Restricted role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }
}
