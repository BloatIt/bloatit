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

import com.bloatit.model.Member;

/**
 * The Class MemberRight store the properties accessor for the {@link Member}
 * class.
 */
public class RgtMember extends RightManager {

    /**
     * The Class Karma is a {@link RightManager.Public} accessor for the Karma
     * property.
     */
    public static class Karma extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class Password is a {@link RightManager.Private} accessor for the
     * Password property.
     */
    public static class Password extends Private {
        // nothing this is just a rename.
    }

    /**
     * The Class Email is a {@link RightManager.Private} accessor for the Email
     * property.
     */
    public static class Email extends Private {
        // nothing this is just a rename.
    }

    /**
     * The Class Locale is a {@link RightManager.Private} accessor for the
     * Locale property.
     */
    public static class Locale extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class Name is a {@link RightManager.Public} accessor for the Name
     * property.
     */
    public static class Name extends Public {
        // nothing this is just a rename.
    }

    public static class Contributions extends Public {
        // nothing this is just a rename.
    }
    
    /**
     * The Class <code>Team</code> is a {@link RightManager.Public} accessor for the
     * <code>Team</code> property.
     */
    public static class Team extends Public {
        // nothing this is just a rename.
    }
}
