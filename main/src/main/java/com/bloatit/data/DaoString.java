package com.bloatit.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

@Embeddable
public class DaoString {

    @Column(columnDefinition = "TEXT", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String content;

    @ManyToOne(optional = false, cascade = { CascadeType.ALL }, fetch=FetchType.LAZY)
    private DaoVersionedString versions;

    public DaoString(final String content, final DaoMember author) {
        this.content = content;
        this.versions = DaoVersionedString.createAndPersist(content, author);
    }

    public final String getContent() {
        return content;
    }

    public final void setContent(final String content, final DaoMember author) {
        versions.addVersion(content, author);
        this.content = content;
    }

    public final DaoVersionedString getVersions() {
        return versions;
    }

    protected DaoString() {
        super();
    }

}
