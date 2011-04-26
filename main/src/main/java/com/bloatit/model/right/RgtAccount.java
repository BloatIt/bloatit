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

import com.bloatit.model.Account;

/**
 * The Class AccountRight store the properties accessor for the {@link Account}
 * class.
 */
public class RgtAccount extends RightManager {

    /**
     * The Class Transaction is a {@link RightManager.PrivateReadOnly} accessor for the
     * Transaction property.
     */
    public static class Transaction extends PrivateReadOnly {
        // nothing this is just a rename.
    }

    /**
     * The Class Amount is a {@link RightManager.PublicReadOnly} accessor for the Amount
     * property.
     */
    public static class Amount extends PrivateReadOnly {
        // nothing this is just a rename.
    }

    /**
     * The Class LastModificationDate is a {@link RightManager.PrivateReadOnly} accessor for
     * the LastModificationDate property.
     */
    public static class LastModificationDate extends PrivateReadOnly {
        // nothing this is just a rename.
    }

    /**
     * The Class CreationDate is a {@link RightManager.PublicReadOnly} accessor for the
     * CreationDate property.
     */
    public static class CreationDate extends PrivateReadOnly {
        // nothing this is just a rename.
    }

    /**
     * The Class Actor is a {@link RightManager.PublicReadOnly} accessor for the Actor
     * property.
     */
    public static class Actor extends PrivateReadOnly {
        // nothing this is just a rename.
    }
}
