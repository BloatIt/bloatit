package com.bloatit.model.data;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DaoUserContent extends DaoIdentifiable {

    @ManyToOne(optional = false)
    private DaoMember member;
    @ManyToOne(optional = true)
    private DaoGroup asGroup;

    @Basic(optional = false)
    private Date creationDate;

    protected DaoUserContent() {
        creationDate = new Date();
    }

    public DaoUserContent(DaoMember member) {
        super();
        // TODO make sure member != null
        this.member = member;
        creationDate = new Date();
    }

    /**
     * No final because it is depreciated for hibernate. but you should
     * considered me as final
     */
    public DaoMember getAuthor() {
        return member;
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

    protected void setAuthor(DaoMember author) {
        member = author;
    }

    protected void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
