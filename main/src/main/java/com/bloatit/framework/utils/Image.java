/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.framework.utils;

/**
 * A container for images in the bloatit website
 */
public class Image {

    /**
     * The storage place of the image LOCAL : if BloatIt server store the image
     * DISTANT : if the image is stored on a distant webserver
     */
    public enum ImageType {
        LOCAL, DISTANT
    }

    private final ImageType imageType;

    /**
     * @param identifier the identifier of the image. If image is local, the
     *            unique name of the image If image is distant, the complete URI
     *            to the image
     * @param imageType LOCAL or DISTANT
     */
    private final String identifier;

    public Image(final String identifier, final ImageType imageType) {
        this.imageType = imageType;
        this.identifier = identifier;
    }

    /**
     * @return <i>true</i> if the image is locally stored on the BloatIt servers
     *         <i>false</i> otherwise.
     */
    public final boolean isLocal() {
        return this.imageType == ImageType.LOCAL;
    }

    /**
     * Gives the identifier of the image. - If the image is local, its
     * identifier is its unique name on the bloatit server. - If the image is
     * distant, its identifier is its complet URI.
     * <p>
     * Should <i>always</i> be used <i>after</i> a call to isLocal.
     * </p>
     * 
     * @return the identifier of the image
     * @see Image#isLocal()
     */
    public final String getIdentifier() {
        return this.identifier;
    }
}
