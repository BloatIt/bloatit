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

import com.bloatit.model.ExternalAccount;

/**
 * The Class ExternalAccountRight store the properties accessor for the
 * {@link ExternalAccount} class.
 */
public class ExternalAccountRight extends RightManager {

    /**
     * The Class BankCode is a {@link Private} accessor for the BankCode
     * property.
     */
    public static class BankCode extends Private {
        // nothing this is just a rename.
    }

    /**
     * The Class Type is a {@link Private} accessor for the Type property.
     */
    public static class Type extends Private {
        // nothing this is just a rename.
    }
}
