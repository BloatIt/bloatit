package com.bloatit.framework.meta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bloatit.framework.FrameworkConfiguration;

public class MetaBug {
    private final String id;
    private final String description;

    public MetaBug(File file) throws IOException {
        this(file.getName(), fileToString(file));
    }

    public MetaBug(String id, String description) {
        this.id = id;
        this.description = description;

    }

    public void update(String newDescription) throws IOException {
        File file = new File(FrameworkConfiguration.getMetaBugsDirStorage() +"/"+ id);

        FileOutputStream fileStream = new FileOutputStream(file);
        fileStream.write(newDescription.getBytes());
        fileStream.close();
    }

    public void delete() {
        File file = new File(FrameworkConfiguration.getMetaBugsDirStorage() +"/"+ id);
        file.renameTo(new File(FrameworkConfiguration.getMetaBugsDirStorage() +"/"+ id + "-deleted"));
    }

    public static String fileToString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(file));
        f.read(buffer);
        return new String(buffer);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}