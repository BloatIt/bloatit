package com.bloatit.framework;

import java.io.File;

import com.bloatit.model.data.DaoFileMetadata;
import com.bloatit.model.data.DaoFileMetadata.FileType;
import com.bloatit.model.data.DaoUserContent;

public class FileMetadata extends UserContent {

    private final DaoFileMetadata dao;

    public static FileMetadata create(DaoFileMetadata file) {
        if (file != null) {
            return new FileMetadata(file);
        }
        return null;
    }

    private FileMetadata(DaoFileMetadata dao) {
        super();
        this.dao = dao;
    }

    @Override
    protected DaoUserContent getDaoUserContent() {
        return dao;
    }

    /**
     * @param shortDescription
     * @see com.bloatit.model.data.DaoFileMetadata#setShortDescription(java.lang.String)
     */
    public final void setShortDescription(String shortDescription) {
        dao.setShortDescription(shortDescription);
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getShortDescription()
     */
    public final String getShortDescription() {
        return dao.getShortDescription();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getFilePath()
     */
    public final String getFilePath() {
        return dao.getFilePath();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getFilename()
     */
    public final File getFile() {
        return new File(dao.getFilename());
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getFolder()
     */
    public final String getFolder() {
        return dao.getFolder();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getSize()
     */
    public final int getSize() {
        return dao.getSize();
    }

    /**
     * @return
     * @see com.bloatit.model.data.DaoFileMetadata#getType()
     */
    public FileType getType() {
        return dao.getType();
    }

    // public UserContent getRelatedContent() {
    // return UserContent.create(dao.getRelatedContent());
    // }

}
