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

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFollowFeature;
import com.bloatit.data.DaoMember;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class FollowFeature extends Identifiable<DaoFollowFeature> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoFollowFeature, FollowFeature> {
        @SuppressWarnings("synthetic-access")
        @Override
        public FollowFeature doCreate(final DaoFollowFeature dao) {
            return new FollowFeature(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static FollowFeature create(final DaoFollowFeature dao) {
        return new MyCreator().create(dao);
    }

    private FollowFeature(final DaoFollowFeature id) {
        super(id);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final DaoMember getFollower() {
        return getDao().getFollower();
    }

    public final DaoFeature getFollowed() {
        return getDao().getFollowed();
    }

    public final boolean isFeatureComment() {
        return getDao().isFeatureComment();
    }

    public final boolean isBugComment() {
        return getDao().isBugComment();
    }

    public final boolean isMail() {
        return getDao().isMail();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final void setFeatureComment(boolean featureComment) {
        getDao().setFeatureComment(featureComment);
    }

    public final void setBugComment(boolean bugComment) {
        getDao().setBugComment(bugComment);
    }

    public final void setMail(boolean mail) {
        getDao().setMail(mail);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return null;
    }

}