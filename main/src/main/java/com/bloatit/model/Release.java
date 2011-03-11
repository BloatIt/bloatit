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

    Release(final Member member, final Milestone milestone, final String description, final String version, final Locale locale) {
        this(DaoRelease.createAndPersist(member.getDao(), milestone.getDao(), description, version, locale));
    }

    public Milestone getMilestone() {
        return Milestone.create(getDao().getMilestone());
    }

    public String getDescription() {
        return getDao().getDescription();
    }

    public Locale locale() {
        return getDao().getLocale();
    }

    public Comment getLastComment() {
        return Comment.create(getDao().getLastComment());
    }

    public PageIterable<Comment> getComments() {
        return new ListBinder<Comment, DaoComment>(getDao().getComments());
    }

    public Feature getFeature() {
        return getMilestone().getOffer().getFeature();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public String getVersion() {
        return getDao().getVersion();
    }



}
