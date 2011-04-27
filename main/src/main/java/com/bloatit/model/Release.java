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
        @SuppressWarnings("synthetic-access")
        @Override
        public Release doCreate(final DaoRelease dao) {
            return new Release(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Release create(final DaoRelease dao) {
        return new MyCreator().create(dao);
    }

    private Release(final DaoRelease dao) {
        super(dao);
    }

    Release(final Member member, final Team team, final Milestone milestone, final String description, final String version, final Locale locale) {
        this(DaoRelease.createAndPersist(member.getDao(), DaoGetter.getTeam(team), milestone.getDao(), description, version, locale));
    }

    // no right management: this is public data
    public Milestone getMilestone() {
        return Milestone.create(getDao().getMilestone());
    }

    // no right management: this is public data
    public String getDescription() {
        return getDao().getDescription();
    }

    // no right management: this is public data
    public Locale getLocale() {
        return getDao().getLocale();
    }

    // no right management: this is public data
    public Comment getLastComment() {
        return Comment.create(getDao().getLastComment());
    }

    // no right management: this is public data
    public PageIterable<Comment> getComments() {
        return new ListBinder<Comment, DaoComment>(getDao().getComments());
    }

    // no right management: this is public data
    public Feature getFeature() {
        return getMilestone().getOffer().getFeature();
    }

    // no right management: this is public data
    public String getVersion() {
        return getDao().getVersion();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
