package com.bloatit.model.admin;

import java.util.Date;

import com.bloatit.data.DaoUserContent;
import com.bloatit.model.Identifiable;
import com.bloatit.model.Member;
import com.bloatit.rest.resources.ModelClassVisitor;

public class UserContentAdmin<T extends DaoUserContent> extends Identifiable<T> {

    protected UserContentAdmin(final T dao) {
        super(dao);
    }

    public static UserContentAdmin<DaoUserContent> create(final DaoUserContent dao) {
        if (dao != null) {
            return new UserContentAdmin<DaoUserContent>(dao);
        }
        return null;
    }

    public static UserContentAdmin<DaoUserContent> createUserContent(final Integer id) {
        final UserContentAdminListFactory.DefaultFactory factory = new UserContentAdminListFactory.DefaultFactory();
        factory.idEquals(id);
        if (factory.list().iterator().hasNext()) {
            return factory.list().iterator().next();
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

    @Override
    protected boolean isMine(final Member member) {
        return false;
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return null;
    }
}
