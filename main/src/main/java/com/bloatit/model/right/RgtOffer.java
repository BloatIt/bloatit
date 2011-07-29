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

import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.Offer;

/**
 * The Class OfferRight store the properties accessor for the {@link Offer}
 * class.
 */
public class RgtOffer extends RightManager {

    /**
     * The Class DateExpire is a {@link RightManager.Public} accessor for the
     * DateExpire property.
     */
    public static class DateExpire extends Public {
        // nothing this is just a rename.
    }

    /**
     * <p>
     * This class is used in {@link FeatureImplementation} because this is the
     * feature that store the selected offer.
     * </p>
     * The Class <code>SelectedOffer</code> is a {@link RightManager.Public}
     * accessor for the <code>SelectedOffer</code> property.
     */
    public static class SelectedOffer extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class <code>Milestone</code> is a {@link RightManager.Public}
     * accessor for the <code>Milestone</code> property.
     */
    public static class Milestone extends Public {
        // nothing this is just a rename.
    }

    /**
     * The Class <code>Draft</code> is a {@link RightManager.Public} accessor
     * for the <code>Draft</code> property.
     */
    public static class Draft extends Public {
        // nothing this is just a rename.
    }

}
