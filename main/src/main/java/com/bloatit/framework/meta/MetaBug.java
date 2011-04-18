package com.bloatit.framework.meta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public class MetaBug {
    private final String id;
    private final String description;

    public MetaBug(final File file) throws IOException {
        this(file.getName(), fileToString(file));
    }

    public MetaBug(final String id, final String description) {
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

    public static String fileToString(final File file) throws IOException {
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
