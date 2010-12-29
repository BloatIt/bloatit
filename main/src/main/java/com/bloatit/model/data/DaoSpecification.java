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
        // TODO test me more.
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

    protected final DaoDemand getDemand() {
        return demand;
    }
}
