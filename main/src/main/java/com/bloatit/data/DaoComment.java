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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A comment is a Kudosable content. It cannot be translated.
 */
@Entity
public final class DaoComment extends DaoKudosable {

    // WARNING "TEXT" is not a standard SQL type.
    @Column(columnDefinition = "TEXT", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String text;

    /**
     * Any comment can have some child comments.
     */
    @OneToMany(mappedBy = "father")
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded(depth = 1)
    private final Set<DaoComment> children = new HashSet<DaoComment>(0);

    public static DaoComment createAndPersist(final DaoMember member, final String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment comment = new DaoComment(member, text);
        try {
            session.save(comment);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return comment;
    }

    /**
     * Create a comment. This constructor is protected because you should use the
     * createAndPersist method (to make sure your comment really goes into the db.
     * 
     * @param member is the author.
     * @param text is the content.
     * @throws NonOptionalParameterException if the text is null
     * @see DaoKudosable#DaoKudosable(DaoMember)
     */
    private DaoComment(final DaoMember member, final String text) {
        super(member);
        if (text == null || text.isEmpty()) {
            throw new NonOptionalParameterException();
        }
        this.text = text;
    }

    /**
     * @throws NonOptionalParameterException if the comment is null.
     */
    public void addChildComment(final DaoComment comment) {
        if (comment == null) {
            throw new NonOptionalParameterException();
        }
        if (comment == this) {
            throw new FatalErrorException("Cannot add ourself as child comment.");
        }
        comment.father = this;
        children.add(comment);
    }

    public String getText() {
        return text;
    }

    /**
     * Use a HQL query to return the children of this comment. It allows the use of
     * PageIterable. Order by creation date, older first.
     * 
     * @return the list of this comment children. return an empty list if there is no
     *         child.
     */
    public PageIterable<DaoComment> getChildren() {
        return new QueryCollection<DaoComment>("from DaoComment as c where c.father = :this order by c.creationDate asc, id").setEntity("this", this);
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    /**
     * You should never use this attribute. It is for hibernate only.
     */
    @ManyToOne(optional = true)
    private DaoComment father;

    protected DaoComment() {
        super();
    }

    protected DaoComment getFather() {
        return father;
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        final DaoComment other = (DaoComment) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

}
