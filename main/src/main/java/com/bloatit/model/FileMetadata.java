package com.bloatit.model;

import java.io.File;

import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.DaoUserContent;

public class FileMetadata extends UserContent {

    private final DaoFileMetadata dao;

    public static FileMetadata create(final DaoFileMetadata file) {
        if (file != null) {
            return new FileMetadata(file);
        }
        return null;
    }

    private FileMetadata(final DaoFileMetadata dao) {
        super();
        this.dao = dao;
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

    /**
     * @param shortDescription
     * @see com.bloatit.data.DaoFileMetadata#setShortDescription(java.lang.String)
     */
    public final void setShortDescription(final String shortDescription) {
        dao.setShortDescription(shortDescription);
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getShortDescription()
     */
    public final String getShortDescription() {
        return dao.getShortDescription();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getFilePath()
     */
    public final String getFilePath() {
        return dao.getFilePath();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getFilename()
     */
    public final File getFile() {
        return new File(dao.getFilename());
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getFolder()
     */
    public final String getFolder() {
        return dao.getFolder();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getSize()
     */
    public final int getSize() {
        return dao.getSize();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoFileMetadata#getType()
     */
    public FileType getType() {
        return dao.getType();
    }

    // public UserContent getRelatedContent() {
    // return UserContent.create(dao.getRelatedContent());
    // }

}
