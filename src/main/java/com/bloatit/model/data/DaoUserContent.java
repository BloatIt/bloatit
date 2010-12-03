package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.bloatit.common.Log;

/**
 * A user content is a content created by a user. There is no table DaoUserContent (the
 * attributes are copied in the sub classes)
 * 
 * A user content as an Author, and can be posted in the name of a group. It also has a
 * creation date.
 * 
 */
@MappedSuperclass
public abstract class DaoUserContent extends DaoIdentifiable {

    /**
     * This is the author of the user content.
     */
    @ManyToOne(optional = false)
    private DaoMember member;

    /**
     * Most of the time this is null. But when a user create a content in the name of a
     * group, asGroup point on it.
     */
    @ManyToOne(optional = true)
    private DaoGroup asGroup;

    @Basic(optional = false)
    private Date creationDate;

    /**
     * Initialize the creation date to now.
     * 
     * @param member is the author of this UserContent.
     * @throws NullPointerException if the member == null.
     */
    public DaoUserContent(DaoMember member) {
        super();
        if (member == null) {
            Log.data().fatal("Cannot create a DaoUserContent with a null member.");
            throw new NullPointerException();
        }
        this.member = member;
        creationDate = new Date();
    }

    public DaoMember getAuthor() {
        return member;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public DaoGroup getAsGroup() {
        return asGroup;
    }

    /**
     * null is the default value and means that the content has a member as author.
     */
    public void setAsGroup(DaoGroup asGroup) {
        this.asGroup = asGroup;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected DaoUserContent() {
        creationDate = new Date();
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setAuthor(DaoMember author) {
        member = author;
    }

    /**
     * This is only for Hibernate. You should never use it.
     */
    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
