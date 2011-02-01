package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.FileMetadataList;

public abstract class UserContent<T extends DaoUserContent> extends Identifiable<T> {

    protected UserContent(final T dao) {
        super(dao);
    }

    protected abstract DaoUserContent getDaoUserContent();

    public final Member getAuthor() {
        return Member.create(getDaoUserContent().getAuthor());
    }

    public final Date getCreationDate() {
        return getDaoUserContent().getCreationDate();
    }

    public final void setAsGroup(final Group asGroup) {
        getDaoUserContent().setAsGroup(asGroup.getDao());
    }

    public final Group getAsGroup() {
        return Group.create(getDaoUserContent().getAsGroup());
    }

    public PageIterable<FileMetadata> getFiles() {
        return new FileMetadataList(getDaoUserContent().getFiles());
    }

}
