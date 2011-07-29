//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoRelease;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

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
        this(DaoRelease.createAndPersist(member.getDao(), DaoGetter.get(team), milestone.getDao(), description, version, locale));
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

    @Override
    public void delete() throws UnauthorizedOperationException {
        if (isDeleted()) {
            return;
        }

        if (!getRights().hasAdminUserPrivilege()) {
            throw new UnauthorizedOperationException(SpecialCode.ADMIN_ONLY);
        }

        super.delete();

        for (final Comment comment : getComments()) {
            comment.delete();
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
