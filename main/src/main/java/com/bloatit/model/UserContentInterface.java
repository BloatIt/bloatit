package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.framework.utils.PageIterable;

public interface UserContentInterface<T extends DaoUserContent> extends IdentifiableInterface, UnlockableInterface {

    Member getAuthor();

    Date getCreationDate();

    void setAsGroup(final Group asGroup);

    Group getAsGroup();

    PageIterable<FileMetadata> getFiles();

}