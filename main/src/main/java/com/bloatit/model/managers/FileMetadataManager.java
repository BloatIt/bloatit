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
package com.bloatit.model.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.bloatit.common.Log;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.lists.FileMetadataList;

/**
 * The Class FileMetadataManager is an utility class containing static methods
 * for {@link FileMetadata} loading etc.
 */
public final class FileMetadataManager {

    /** The Constant FILE_STORAGE_DIRECTORY. */
    private final static String FILE_STORAGE_DIRECTORY = FrameworkConfiguration.getRessourcesDirStorage();

    /**
     * Desactivated constructor on utility class.
     */
    private FileMetadataManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the file metadata by id.
     *
     * @param id the id
     * @return the file metadata or null if not found.
     */
    public static FileMetadata getById(final Integer id) {
        return FileMetadata.create(DBRequests.getById(DaoFileMetadata.class, id));
    }

    /**
     * Creates a new {@link FileMetadata} using a temporary file. This file will
     * move the temporary file to the definitive storage folder.
     *
     * @param author the author of the new {@link FileMetadata}
     * @param tempFileUrl the url to the temporary file.
     * @param filename the filename the file name to display in the web site
     *            (may be different from the real file name).
     * @param description a short description coming with the new
     *            {@link FileMetadata}.
     * @return the newly created {@link FileMetadata}
     */
    public static FileMetadata createFromTempFile(final Member author, final String tempFileUrl, final String filename, final String description) {

        createWipDirectory();

        final File tempFile = new File(tempFileUrl);

        final File storedFile = new File(FILE_STORAGE_DIRECTORY + "/" + tempFile.getName());
        tempFile.renameTo(storedFile);

        return createFileMetadata(author, filename, description, storedFile);

    }

    /**
     * Make sure there is a directory to store the files.
     */
    private static final void createWipDirectory() {
        final File stroreDir = new File(FILE_STORAGE_DIRECTORY);
        if (!stroreDir.exists()) {
            stroreDir.mkdirs();
            Log.model().info("Created directory " + FILE_STORAGE_DIRECTORY);
        }
    }

    /**
     * Same as {@link #createFromTempFile(Member, String, String, String)} but
     * with a copy.
     *
     * @param author the author of the new {@link FileMetadata}
     * @param path the url to the local file.
     * @param name the filename the file name to display in the web site (may be
     *            different from the real file name).
     * @param description a short description coming with the new
     *            {@link FileMetadata}.
     * @return the newly created {@link FileMetadata}
     */
    public static FileMetadata createFromLocalFile(final Member author, final String path, final String name, final String description) {
        createWipDirectory();

        final File tempFile = new File(path);

        final File storedFile = new File(FILE_STORAGE_DIRECTORY + "/" + tempFile.getName());
        try {
            copyFile(tempFile, storedFile);

            return createFileMetadata(author, name, description, storedFile);

        } catch (final IOException e) {
            Log.model().error("Copy failed", e);
            return null;
        }

    }

    /**
     * Create a new fileMetadata.
     *
     * @param author
     * @param name
     * @param description
     * @param storedFile
     * @return
     */
    private static FileMetadata createFileMetadata(final Member author, final String name, final String description, final File storedFile) {
        // TODO: improve mine type detection
        FileType type = FileType.UNKNOWN;
        if (storedFile.getName().endsWith(".txt")) {
            type = FileType.TEXT;
        } else if (storedFile.getName().endsWith(".html")) {
            type = FileType.HTML;
        } else if (storedFile.getName().endsWith(".tex")) {
            type = FileType.TEX;
        } else if (storedFile.getName().endsWith(".pdf")) {
            type = FileType.PDF;
        } else if (storedFile.getName().endsWith(".odt")) {
            type = FileType.ODT;
        } else if (storedFile.getName().endsWith(".doc")) {
            type = FileType.DOC;
        } else if (storedFile.getName().endsWith(".bmp")) {
            type = FileType.BMP;
        } else if (storedFile.getName().endsWith(".jpg")) {
            type = FileType.JPG;
        } else if (storedFile.getName().endsWith(".png")) {
            type = FileType.PNG;
        } else if (storedFile.getName().endsWith(".svg")) {
            type = FileType.SVG;
        }

        final FileMetadata file = new FileMetadata(author, name, storedFile.getPath(), type, (int) storedFile.length());
        file.setShortDescription(description);
        return file;
    }

    /**
     * Copy a file.
     *
     * @param in the original file.
     * @param out the file to where to copy <code>in</code>.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copyFile(final File in, final File out) throws IOException {
        final FileChannel inChannel = new FileInputStream(in).getChannel();
        final FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (final IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * @return
     */
    public static PageIterable<FileMetadata> getAll() {
        return new FileMetadataList(DBRequests.getAll(DaoFileMetadata.class));
    }

}
