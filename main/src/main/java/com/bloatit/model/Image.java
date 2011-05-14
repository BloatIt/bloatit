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

package com.bloatit.model;



/**
 * A container for images in the bloatit website
 */
public class Image implements com.bloatit.framework.model.Image {
    private String identifier;
    private FileMetadata metadata;

    /**
     * Creates an image with a default image type local
     * 
     * @param identifier
     */
    public Image(final String identifier) {
        this.identifier = identifier;
    }

    public Image(final FileMetadata metadata) {
        this.metadata = metadata;
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
     */
    @Override
    public final String getIdentifier() {
        if (metadata != null) {
            return metadata.getUrl();
        }
        return this.identifier;
    }

    public FileMetadata getMetadata() {
        return metadata;
    }

    @Override
    public boolean isNull() {
        return this.metadata == null;
    }
}
