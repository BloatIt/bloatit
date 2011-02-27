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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DaoComment extends DaoKudosable implements DaoCommentable {

    // WARNING "TEXT" is not a standard SQL type.
    @Column(columnDefinition = "TEXT", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String text;

    @ManyToOne
    private DaoComment father;
    @ManyToOne
    private DaoBug bug;
    @ManyToOne
    private DaoDemand demand;
    @ManyToOne
    private DaoRelease release;

    /**
     * Any comment can have some child comments.
     */
    @OneToMany(mappedBy = "father")
    @Cascade(value = { CascadeType.ALL })
    //@OrderBy("id")
    @IndexedEmbedded(depth = 1)
    private List<DaoComment> children = new ArrayList<DaoComment>(0);

    public static DaoComment createAndPersist(final DaoBug father, final DaoMember member, final String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment demand = new DaoComment(father, member, text);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return demand;
    }

    public static DaoComment createAndPersist(final DaoDemand father, final DaoMember member, final String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment demand = new DaoComment(father, member, text);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return demand;
    }

    public static DaoComment createAndPersist(final DaoRelease father, final DaoMember member, final String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment demand = new DaoComment(father, member, text);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return demand;
    }

    public static DaoComment createAndPersist(final DaoComment father, final DaoMember member, final String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment demand = new DaoComment(father, member, text);
        try {
            session.save(demand);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            SessionManager.getSessionFactory().getCurrentSession().beginTransaction();
            throw e;
        }
        return demand;
    }

    /**
     * Create a comment. This constructor is protected because you should use
     * the createAndPersist method (to make sure your comment really goes into
     * the db.
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

    private DaoComment(final DaoBug father, final DaoMember member, final String text) {
        this(member, text);
        this.bug = father;
    }

    private DaoComment(final DaoDemand father, final DaoMember member, final String text) {
        this(member, text);
        this.demand = father;
    }

    private DaoComment(final DaoRelease father, final DaoMember member, final String text) {
        this(member, text);
        this.release = father;
    }

    private DaoComment(final DaoComment father, final DaoMember member, final String text) {
        this(member, text);
        this.father = father;
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
        this.children.add(comment);
    }

    public String getText() {
        return this.text;
    }

    /**
     * Use a HQL query to return the children of this comment. It allows the use
     * of PageIterable. Order by creation date, older first.
     * 
     * @return the list of this comment children. return an empty list if there
     *         is no child.
     */
    public PageIterable<DaoComment> getChildren() {
        return new QueryCollection<DaoComment>("from DaoComment as c where c.father = :this order by c.creationDate asc, id").setEntity("this", this);
    }

    @Override
    public PageIterable<DaoComment> getComments() {
        return new MappedList<DaoComment>(this.children);
    }

    @Override
    public DaoComment getLastComment() {
        return this.children.get(this.children.size() - 1);
    }

    @Override
    public void addComment(final DaoComment comment) {
        addChildComment(comment);
    }

    public DaoUserContent getCommented() {
        if (father != null) {
            return father;
        }
        if (bug != null) {
            return bug;
        }
        if (demand != null) {
            return demand;
        }
        if (release != null) {
            return release;
        }
        return null;
    }

    protected DaoComment getFather() {
        return this.father;
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    protected DaoComment() {
        super();
    }

    // ======================================================================
    // equals and hashcode
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
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
        final DaoComment other = (DaoComment) obj;
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!this.text.equals(other.text)) {
            return false;
        }
        return true;
    }

}
