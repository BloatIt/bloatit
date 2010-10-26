package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class UserContent extends Identifiable {

    // TODO find why I cannot make this not-null (Group related)
    @ManyToOne
    private Actor actor;

    // TODO I would like to have some external join tables.
    @ManyToOne(optional = true)
    private Group asGroup;
    @Basic(optional = false)
    private Date creationDate;

    protected UserContent() {
        creationDate = new Date();
    }

    public UserContent(Actor actor) {
        super();
        this.actor = actor;
        creationDate = new Date();
    }

    /**
     * No final because it is depreciated for hibernate. but you should
     * considered me as final
     */
    public Actor getAuthor() {
        return actor;
    }

    /**
     * No final because it is depreciated for hibernate. but you should
     * considered me as final
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setAsGroup(Group asGroup) {
        this.asGroup = asGroup;
    }

    public Group getAsGroup() {
        return asGroup;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setAuthor(Actor author) {
        this.actor = author;
    }

    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
