package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DaoUserContent extends DaoIdentifiable {

    // TODO find why I cannot make this not-null (DaoGroup related)
    @ManyToOne
    private DaoActor actor;

    // TODO I would like to have some external join tables.
    @ManyToOne(optional = true)
    private DaoGroup asGroup;
    @Basic(optional = false)
    private Date creationDate;

    protected DaoUserContent() {
        creationDate = new Date();
    }

    public DaoUserContent(DaoActor Actor) {
        super();
        this.actor = Actor;
        creationDate = new Date();
    }

    /**
     * No final because it is depreciated for hibernate. but you should
     * considered me as final
     */
    public DaoActor getAuthor() {
        return actor;
    }

    /**
     * No final because it is depreciated for hibernate. but you should
     * considered me as final
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public void setAsGroup(DaoGroup asGroup) {
        this.asGroup = asGroup;
    }

    public DaoGroup getAsGroup() {
        return asGroup;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected void setAuthor(DaoActor author) {
        this.actor = author;
    }

    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
