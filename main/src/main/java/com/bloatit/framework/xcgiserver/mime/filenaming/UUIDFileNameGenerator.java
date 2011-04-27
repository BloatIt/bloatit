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
package com.bloatit.framework.xcgiserver.mime.filenaming;

import java.util.UUID;

/**
 * A File name generator that generates a random name based on UUID and adds to
 * it the extension of the original filename
 */
public class UUIDFileNameGenerator implements FileNamingGenerator {
    private static final char SEPARATOR = '.';

    @Override
    public String generateName(final String fileName) {
        final UUID uuid = UUID.randomUUID();
        final int separatorIndex = fileName.lastIndexOf(SEPARATOR);
        if (separatorIndex == -1) {
            return uuid.toString();
        }

        return uuid.toString() + fileName.substring(separatorIndex, fileName.length());
    }
}
