package com.bloatit.model.admin;

import java.util.Date;

import com.bloatit.data.DaoUserContent;
import com.bloatit.model.Identifiable;

public class UserContentAdministration<T extends DaoUserContent> extends Identifiable<T> {

    protected UserContentAdministration(final T dao) {
        super(dao);
    }

    public static UserContentAdministration create(final DaoUserContent dao) {
        if (dao != null) {
            return new UserContentAdministration(dao);
        }
        return null;
    }

    public final String getAuthor() {
        return getDao().getAuthor().getLogin();
    }

    public final Date getCreationDate() {
        return getDao().getCreationDate();
    }

    public final String getAsGroup() {
        if (getDao().getAsGroup() != null) {
            return getDao().getAsGroup().getLogin();
        }
        return "null";
    }

    public final int getFilesNumber() {
        return getDao().getFiles().size();
    }

    public Boolean isDeleted() {
        return getDao().isDeleted();
    }

    public String getType() {
        return getDao().getClass().getCanonicalName().replaceAll(".*\\.", "");
    }

    // ////////////////////////////////////////////////////////////////////////
    // Modifiers
    // ////////////////////////////////////////////////////////////////////////

    public void delete() {
        getDao().setIsDeleted(true);
    }

    public void restore() {
        getDao().setIsDeleted(false);
    }
}
