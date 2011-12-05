//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Actor Foundation, either version 3 of the License, or (at your
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

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoFollowActor;
import com.bloatit.data.DaoMember;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class FollowActor extends Identifiable<DaoFollowActor> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFollowActor, FollowActor> {
        @SuppressWarnings("synthetic-access")
        @Override
        public FollowActor doCreate(final DaoFollowActor dao) {
            return new FollowActor(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static FollowActor create(final DaoFollowActor dao) {
        return new MyCreator().create(dao);
    }

    private FollowActor(final DaoFollowActor id) {
        super(id);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final Member getFollower() throws UnauthorizedOperationException {
        return Member.create(getDao().getFollower());
    }

    public final Actor<?> getFollowed() {
        return Actor.getActorFromDao(getDao().getFollowed());
    }

    public final boolean isMail() {
        return getDao().isMail();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final void setMail(boolean mail) throws UnauthorizedOperationException {
        Member follower = Member.create(getDao().getFollower());
        if(!(AuthToken.isAdmin() || (AuthToken.isAuthenticated() && AuthToken.getMember().equals(follower)))) {
            throw new UnauthorizedOperationException(Action.WRITE);
        }
        getDao().setMail(mail);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
