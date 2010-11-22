package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.bloatit.common.Log;
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

/**
 * A comment is a Kudosable content. It cannot be translated.
 */
@Entity
public class DaoComment extends DaoKudosable {

    /**
     * This is the text of the comment. There is no specific format handling (html/wikitag
     * ??)
     */
    @Basic(optional = false)
    @Column(length = 5000)
    // TODO Change the length ! it is really awful...
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String text;

    /**
     * Any comment can have some child comments.
     */
    @OneToMany(mappedBy = "father")
    @Cascade(value = { CascadeType.ALL })
    @OrderBy("creationDate desc")
    @IndexedEmbedded(depth = 1)
    private Set<DaoComment> children = new HashSet<DaoComment>(0);

    public static DaoComment createAndPersist(DaoMember member, String text) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoComment comment = new DaoComment(member, text);
        try {
            session.save(comment);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            Log.data().error(e);
            session.beginTransaction();
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
     * @throws NullPointerException if the text is null
     * @see DaoKudosable#DaoKudosable(DaoMember)
     */
    protected DaoComment(DaoMember member, String text) {
        super(member);
        if (text == null) {
            throw new NullPointerException();
        }
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * Use a HQL query to return the children of this comment. It allows the use of
     * PageIterable.
     * 
     * @return the list of this comment children. return an empty list if there is no
     * child.
     */
    // TODO use a filtered collection
    public PageIterable<DaoComment> getChildrenFromQuery() {
        return new QueryCollection<DaoComment>("from DaoComment as c where c.father = :this").setEntity("this", this);
    }

    protected Set<DaoComment> getChildren() {
        return children;
    }

    /**
     * @throws NullPointerException if the comment is null.
     */
    public void addChildComment(DaoComment comment) {
        // if (comment == null) {
        // throw new NullPointerException();
        // }
        // next line throw the NullPointerException...
        comment.setFather(this);
        children.add(comment);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * You should never use this attribute. It is for hibernate only.
     */
    @ManyToOne(optional = true)
    private DaoComment father;

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoComment() {
        super();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setText(String text) {
        this.text = text;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setChildren(Set<DaoComment> children) {
        this.children = children;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setFather(DaoComment Comment) {
        father = Comment;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoComment getFather() {
        return father;
    }
}
