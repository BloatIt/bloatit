package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.common.Log;
import com.bloatit.common.PageIterable;
import com.bloatit.model.data.util.SessionManager;

@Entity
public class DaoComment extends DaoKudosable {

    @Basic(optional = false)
    private String text;
    @OneToMany(mappedBy = "comment", cascade = { CascadeType.ALL })
    @OrderBy(value = "creationDate")
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

    protected DaoComment(DaoMember member, String text) {
        super(member);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // TODO use a filtered collection
    public PageIterable<DaoComment> getChildrenFromQuery() {
        QueryCollection<DaoComment> q = new QueryCollection<DaoComment>(
                "from com.bloatit.model.data.DaoComment as c where c.comment = :this");
        q.setEntity("this", this);
        return q;
    }

    public Set<DaoComment> getChildren() {
        return children;
    }

    public void addChildComment(DaoComment Comment) {
        children.add(Comment);
    }

    protected DaoComment() {
        super();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    @ManyToOne
    private DaoComment comment;

    protected void setText(String text) {
        this.text = text;
    }

    protected void setChildren(Set<DaoComment> children) {
        this.children = children;
    }

    protected void setComment(DaoComment Comment) {
        this.comment = Comment;
    }

    protected DaoComment getComment() {
        return comment;
    }
}
