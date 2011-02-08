/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.model.managers;

import java.io.File;

import com.bloatit.common.Log;
import com.bloatit.data.DBRequests;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.framework.utils.ConfigurationManager;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;


public final class FileMetadataManager {

    private final static String FILE_STORAGE_DIRECTORY = ConfigurationManager.SHARE_DIR + "file_storage";

    private FileMetadataManager() {
        // Desactivate default ctor
    }

    public static FileMetadata getFileMetadataById(final Integer id) {
        return FileMetadata.create(DBRequests.getById(DaoFileMetadata.class, id));
    }

    public static FileMetadata createFromTempFile(final Member author, String tempFileUrl) {

        createWipDirectory();

        File tempFile = new  File(tempFileUrl);

        File storedFile = new  File(FILE_STORAGE_DIRECTORY+"/"+tempFile.getName());
        tempFile.renameTo(storedFile);

        //TODO: improve mine type detection
        FileType type = FileType.UNKNOWN;
        if(storedFile.getName().endsWith(".txt")) {
            type = FileType.TEXT;
        } else if(storedFile.getName().endsWith(".html")) {
            type = FileType.HTML;
        } else if(storedFile.getName().endsWith(".tex")) {
            type = FileType.TEX;
        } else if(storedFile.getName().endsWith(".pdf")) {
            type = FileType.PDF;
        } else if(storedFile.getName().endsWith(".odt")) {
            type = FileType.ODT;
        } else if(storedFile.getName().endsWith(".doc")) {
            type = FileType.DOC;
        } else if(storedFile.getName().endsWith(".bmp")) {
            type = FileType.BMP;
        } else if(storedFile.getName().endsWith(".jpg")) {
            type = FileType.JPG;
        } else if(storedFile.getName().endsWith(".png")) {
            type = FileType.PNG;
        } else if(storedFile.getName().endsWith(".svg")) {
            type = FileType.SVG;
        }


        FileMetadata file = new FileMetadata(author, storedFile.getName(), storedFile.getParent()+"/", type, (int) storedFile.length());

        return file;

    }

    /**
     * Make sure there is a directory to store the files
     */
    private static final void createWipDirectory() {
        final File stroreDir = new File(FILE_STORAGE_DIRECTORY);
        if (!stroreDir.exists()) {
            stroreDir.mkdirs();
            Log.mail().info("Created directory " + FILE_STORAGE_DIRECTORY);
        }
    }

}
