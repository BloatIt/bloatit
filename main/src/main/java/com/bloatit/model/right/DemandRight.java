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

import com.bloatit.model.Demand;

/**
 * The Class DemandRight store the properties accessor for the {@link Demand} class.
 */
public class DemandRight extends RightManager {

    /**
     * The Class Description is an accessor for the Description property.
     */
    public static class Description extends Accessor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.right.Accessor#can(com.bloatit.model.right.RestrictedInterface
         * , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    /**
     * The Class Offer is an accessor for the Offer property.
     */
    public static class Offer extends Accessor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.right.Accessor#can(com.bloatit.model.right.RestrictedInterface
         * , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    /**
     * The Class Specification is an accessor for the Specification property.
     */
    public static class Specification extends Accessor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.right.Accessor#can(com.bloatit.model.right.RestrictedInterface
         * , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    /**
     * The Class Contribute is an accessor for the Contribute property.
     */
    public static class Contribute extends Accessor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.right.Accessor#can(com.bloatit.model.right.RestrictedInterface
         * , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    /**
     * The Class DemandContent is an accessor for the DemandContent property.
     */
    public static class DemandContent extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class Comment is an accessor for the Comment property.
     */
    public static class Comment extends Accessor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.bloatit.model.right.Accessor#can(com.bloatit.model.right.RestrictedInterface
         * , com.bloatit.model.right.Action)
         */
        @Override
        protected final boolean can(final RestrictedInterface role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }
}
