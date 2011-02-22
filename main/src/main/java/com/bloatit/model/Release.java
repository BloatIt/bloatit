package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoRelease;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;

public class Release extends UserContent<DaoRelease> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoRelease, Release> {
        @Override
        public Release doCreate(final DaoRelease dao) {
            return new Release(dao);
        }
    }

    public static Release create(final DaoRelease dao) {
        return new MyCreator().create(dao);
    }

    private Release(DaoRelease dao) {
        super(dao);
    }

    public void addComment(DaoComment comment) {
        getDao().addComment(comment);
    }

    public Batch getBatch() {
        return Batch.create(getDao().getBatch());
    }

    public String getDescription() {
        return getDao().getDescription();
    }

    public Locale getLocale() {
        return getDao().getLocale();
    }

    public Comment getLastComment() {
        return Comment.create(getDao().getLastComment());
    }

    public PageIterable<Comment> getComments() {
        return new ListBinder<Comment, DaoComment>(getDao().getComments());
    }

}
