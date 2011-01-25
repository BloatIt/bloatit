package com.bloatit.model.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import com.bloatit.model.data.util.NonOptionalParameterException;

/**
 * A specification is some resources added to a demand to add some precisions to the
 * description. The specification are not translatable. The specification will probably
 * change a lot ... We will have to implement some version controling.
 */
@Entity
public final class DaoSpecification extends DaoUserContent {

    @Field(index = Index.TOKENIZED, store = Store.NO)
    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne(optional = false)
    private DaoDemand demand;

    /**
     * Create a Specification.
     *
     * @param member is the author of the specification.
     * @param content is the content of the specification ...
     * @param demand yep, this is the demand on which the specification apply. Whhoohooo !
     * @throws NonOptionalParameterException if member or demand are null.
     */
    public DaoSpecification(final DaoMember member, final String content, final DaoDemand demand) {
        super(member);
        if (demand == null) {
            throw new NonOptionalParameterException();
        }
        setContent(content);
        this.demand = demand;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoSpecification() {
        super();
    }

    protected DaoDemand getDemand() {
        return demand;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result + ((demand == null) ? 0 : demand.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DaoSpecification)) {
            return false;
        }
        DaoSpecification other = (DaoSpecification) obj;
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        if (demand == null) {
            if (other.demand != null) {
                return false;
            }
        } else if (!demand.equals(other.demand)) {
            return false;
        }
        return true;
    }
}
