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
package com.bloatit.framework.meta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public class MetaFeedback {
    private final String id;
    private final String description;

    protected MetaFeedback(final File file) throws IOException {
        this(file.getName(), fileToString(file));
    }

    private MetaFeedback(final String id, final String description) {
        this.id = id;
        this.description = description;

    }

    public void update(final String newDescription) throws IOException {
        final File file = new File(FrameworkConfiguration.getMetaBugsDirStorage() + "/" + id);

        final FileOutputStream fileStream = new FileOutputStream(file);
        fileStream.write(newDescription.getBytes());
        fileStream.close();
    }

    public void delete() {
        final File file = new File(FrameworkConfiguration.getMetaBugsDirStorage() + "/" + id);
        if (!file.renameTo(new File(FrameworkConfiguration.getMetaBugsDirStorage() + "/" + id + "-deleted"))) {
            throw new BadProgrammerException("Couldn't move treated bug");
        }
    }

    private static String fileToString(final File file) throws IOException {
        final byte[] buffer = new byte[(int) file.length()];
        final BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        f.close();
        return new String(buffer);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
