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
package com.bloatit.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.NonOptionalParameterException;

@Entity
public class DaoFileMetadata extends DaoUserContent {

    public enum FileType {
        TEXT, HTML, TEX, PDF, ODT, DOC, BMP, JPG, PNG, SVG, UNKNOWN
    }

    @Basic(optional = false)
    private String filename;

    @Basic(optional = false)
    private String url;

    @Basic(optional = false)
    private int size;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Basic(optional = false)
    @Enumerated
    private FileType type;

    @OneToOne(optional = true, mappedBy = "file")
    private DaoImage image;

    public static DaoFileMetadata createAndPersist(final DaoMember member,
                                                   final DaoUserContent relatedContent,
                                                   final String filename,
                                                   final String url,
                                                   final FileType type,
                                                   final int size) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoFileMetadata file = new DaoFileMetadata(member, relatedContent, filename, url, type, size);
        try {
            session.save(file);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            Log.data().error(e);
            session.beginTransaction();
            throw e;
        }
        return file;
    }

    /**
     * @param member is the author (the one who uploaded the file)
     * @param relatedContent can be null. It is the content with which this file has been
     *        uploaded.
     * @param filename is the name of the file (with its extension, but without its whole
     *        folder path)
     * @param directory is the path of the directory where the file is.
     * @param type is the type of the file (found using its extension or mimetype)
     * @param size is the size of the file.
     */
    private DaoFileMetadata(final DaoMember member,
                            final DaoUserContent relatedContent,
                            final String filename,
                            final String url,
                            final FileType type,
                            final int size) {
        super(member);
        if (filename == null || url == null || type == null || filename.isEmpty() || url.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.size = size;
        this.filename = filename;
        this.url = url;
        this.type = type;
        this.shortDescription = null;
        if (relatedContent != null) {
            relatedContent.addFile(this);
        }
        // At the end to make sure the assignment are done.
        // It works only if equal is final !!
        if (equals(relatedContent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param shortDescription the shortDescription to set
     */
    public final void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Tells that the current File is an image. Used in DaoImage constructor.
     *
     * @param image the image to set.
     */
    void setImage(DaoImage image) {
        this.image = image;
    }

    /**
     * If the file is an image, it should be associated with a DaoImage object.
     *
     * @return the image object associated with this file. It can be null.
     */
    public DaoImage getImage() {
        return image;
    }

    /**
     * @return the url.
     */
    public final String getUrl() {
        return url;
    }

    /**
     * @return the shortDescription
     */
    public final String getShortDescription() {
        return shortDescription;
    }

    /**
     * @return the filename
     */
    public final String getFilename() {
        return filename;
    }

    /**
     * @return the size
     */
    public final int getSize() {
        return size;
    }

    public FileType getType() {
        return type;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoFileMetadata() {
        // for hibernate.
    }

    // ======================================================================
    // equals hashcode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoFileMetadata other = (DaoFileMetadata) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        if (filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!filename.equals(other.filename)) {
            return false;
        }
        return true;
    }

}
