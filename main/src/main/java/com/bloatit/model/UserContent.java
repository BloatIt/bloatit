package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.FileMetadataList;

public abstract class UserContent<T extends DaoUserContent> extends Identifiable<T> implements UserContentInterface<T> {

    protected UserContent(final T dao) {
        super(dao);
    }

    protected abstract DaoUserContent getDaoUserContent();

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getAuthor()
     */
    @Override
    public final Member getAuthor() {
        return Member.create(getDaoUserContent().getAuthor());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getCreationDate()
     */
    @Override
    public final Date getCreationDate() {
        return getDaoUserContent().getCreationDate();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#setAsGroup(com.bloatit.model.Group)
     */
    @Override
    public final void setAsGroup(final Group asGroup) {
        getDaoUserContent().setAsGroup(asGroup.getDao());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getAsGroup()
     */
    @Override
    public final Group getAsGroup() {
        return Group.create(getDaoUserContent().getAsGroup());
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.UserContentInterface#getFiles()
     */
    @Override
    public PageIterable<FileMetadata> getFiles() {
        return new FileMetadataList(getDaoUserContent().getFiles());
    }

}
