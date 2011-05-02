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

import com.bloatit.model.Actor;

/**
 * The Class ActorRight store the properties accessor for the {@link Actor}
 * class.
 */
public class RgtActor extends RightManager {

    /**
     * The Class ExternalAccount is a {@link RightManager.Private} accessor for
     * the ExternalAccount property.
     */
    public static class ExternalAccount extends BankData {
        // Nothing
    }

    /**
     * The Class InternalAccount is a {@link RightManager.Private} accessor for
     * the InternalAccount property.
     */
    public static class InternalAccount extends BankData {
        // nothing
    }

    /**
     * The Class BankTransaction is a {@link RightManager.Private} accessor for
     * the BankTransaction property.
     */
    public static class BankTransaction extends BankData {
        // nothing
    }

    /**
     * The Class DateCreation is a {@link RightManager.PublicReadOnly} accessor for the
     * DateCreation property.
     */
    public static class DateCreation extends PublicReadOnly {
        // nothing this is just a rename.
    }
    
    /**
     * The Class Contribution is a {@link RightManager.Public} accessor for the
     * DateCreation property.
     */
    public static class Contribution extends Public {
        // nothing this is just a rename.
    }
}
