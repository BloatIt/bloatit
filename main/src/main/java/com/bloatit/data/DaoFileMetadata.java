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
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * The Class DaoFileMetadata represent a file.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoFileMetadata extends DaoUserContent {

    /**
     * The Enum FileType is the type of a file.
     */
    public enum FileType {

        /** The TEXT. */
        TEXT,
        /** The HTML. */
        HTML,
        /** The TEX. */
        TEX,
        /** The PDF. */
        PDF,
        /** The ODT. */
        ODT,
        /** The DOC. */
        DOC,
        /** The BMP. */
        BMP,
        /** The JPG. */
        JPG,
        /** The PNG. */
        PNG,
        /** The SVG. */
        SVG,
        /** The UNKNOWN. */
        UNKNOWN
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

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private DaoUserContent relatedContent;

    /**
     * Creates the fileMetadata and persist it.
     * 
     * @param member the member author
     * @param team the as team property, can be null
     * @param relatedContent the related content
     * @param filename the filename
     * @param url the url of the file in the filesystem
     * @param type the file type
     * @param size the file size
     * @return the newly created dao file metadata
     */
    public static DaoFileMetadata createAndPersist(final DaoMember member,
                                                   final DaoTeam team,
                                                   final DaoUserContent relatedContent,
                                                   final String filename,
                                                   final String url,
                                                   final FileType type,
                                                   final int size) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoFileMetadata file = new DaoFileMetadata(member, team, relatedContent, filename, url, type, size);
        try {
            session.save(file);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return file;
    }

    /**
     * @param member is the author (the one who uploaded the file)
     * @param relatedContent can be null. It is the content with which this file
     *            has been uploaded.
     * @param filename is the name of the file (with its extension, but without
     *            its whole folder path)
     * @param directory is the path of the directory where the file is.
     * @param type is the type of the file (found using its extension or
     *            mimetype)
     * @param size is the size of the file.
     */
    private DaoFileMetadata(final DaoMember member,
                            final DaoTeam team,
                            final DaoUserContent relatedContent,
                            final String filename,
                            final String url,
                            final FileType type,
                            final int size) {
        super(member, team);
        if (filename == null || url == null || type == null || filename.isEmpty() || url.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.size = size;
        this.filename = filename;
        this.url = url;
        this.type = type;
        this.shortDescription = null;
        this.relatedContent = relatedContent;
        if (relatedContent != null) {
            relatedContent.addFile(this);
        }
        // At the end to make sure the assignment are done.
        // It works only if equal is !!
        if (equals(relatedContent)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the short description.
     * 
     * @param shortDescription the shortDescription to set
     */
    public void setShortDescription(final String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * Gets the url.
     * 
     * @return the url.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Gets the short description.
     * 
     * @return the shortDescription
     */
    public String getShortDescription() {
        return this.shortDescription;
    }

    /**
     * Gets the filename.
     * 
     * @return the filename
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Gets the size.
     * 
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets the file type.
     * 
     * @return the file type
     */
    public FileType getType() {
        return this.type;
    }

    /**
     * Gets the content on which this file is attached.
     * 
     * @return content on which this file is attached.
     */
    public DaoUserContent getRelatedContent() {
        return this.relatedContent;
    }

    /**
     * Sets the content on which this file is attached.
     * 
     * @param relatedContent the content on which this file is attached.
     */
    void setRelatedContent(final DaoUserContent relatedContent) {
        this.relatedContent = relatedContent;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see
     * com.bloatit.data.DaoIdentifiable#accept(com.bloatit.data.DataClassVisitor
     * )
     */
    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * Instantiates a new dao file metadata.
     */
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        result = prime * result + ((this.filename == null) ? 0 : this.filename.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
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
        if (this.url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!this.url.equals(other.url)) {
            return false;
        }
        if (this.filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!this.filename.equals(other.filename)) {
            return false;
        }
        return true;
    }

}
