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

import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoFollowSoftware;
import com.bloatit.data.DaoMember;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class FollowSoftware extends Identifiable<DaoFollowSoftware> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFollowSoftware, FollowSoftware> {
        @SuppressWarnings("synthetic-access")
        @Override
        public FollowSoftware doCreate(final DaoFollowSoftware dao) {
            return new FollowSoftware(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static FollowSoftware create(final DaoFollowSoftware dao) {
        return new MyCreator().create(dao);
    }

    private FollowSoftware(final DaoFollowSoftware id) {
        super(id);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final Member getFollower() throws UnauthorizedOperationException {
        return Member.create(getDao().getFollower());
    }

    public final Software getFollowed() {
        return Software.create(getDao().getFollowed());
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
        for(FollowFeature followFeature: getFollower().getFollowedFeatures()) {
            if (followFeature.getFollowed().getSoftware().equals(getFollowed())) {
                followFeature.setMail(mail);
            }
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
