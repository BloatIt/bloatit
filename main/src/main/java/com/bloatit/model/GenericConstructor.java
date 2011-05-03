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

import org.hibernate.ObjectNotFoundException;

import com.bloatit.common.Log;
import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoHighlightFeature;
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoJoinTeamInvitation;
import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.DaoTranslation;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DBRequests;

public class GenericConstructor {

    public static IdentifiableInterface create(final Class<? extends IdentifiableInterface> clazz, final Integer id) throws ClassNotFoundException {
        // TODO: Crash if not found
        final Class<?> daoClass = getDaoClass(clazz);
        if (daoClass == null) {
            throw new ClassNotFoundException("Cannot find a dao class for the class " + clazz);
        }
        try {
            final DaoIdentifiable byId = (DaoIdentifiable) DBRequests.getById(daoClass, id);
            if (byId != null) {
                return byId.accept(new DataVisitorConstructor());
            }
        } catch(ObjectNotFoundException e) {
            return null;
        }


        return null;
    }

    private static Class<?> getDaoClass(final Class<? extends IdentifiableInterface> clazz) {
        if (clazz.equals(ExternalAccount.class)) {
            return DaoExternalAccount.class;
        }
        if (clazz.equals(InternalAccount.class)) {
            return DaoInternalAccount.class;
        }
        if (clazz.equals(Member.class)) {
            return DaoMember.class;
        }
        if (clazz.equals(Team.class)) {
            return DaoTeam.class;
        }
        if (clazz.equals(Actor.class)) {
            return DaoActor.class;
        }
        if (clazz.equals(BankTransaction.class)) {
            return DaoBankTransaction.class;
        }
        if (clazz.equals(Milestone.class)) {
            return DaoMilestone.class;
        }
        if (clazz.equals(Description.class)) {
            return DaoDescription.class;
        }
        if (clazz.equals(HighlightFeature.class)) {
            return DaoHighlightFeature.class;
        }
        if (clazz.equals(JoinTeamInvitation.class)) {
            return DaoJoinTeamInvitation.class;
        }
        if (clazz.equals(Software.class)) {
            return DaoSoftware.class;
        }
        if (clazz.equals(Transaction.class)) {
            return DaoTransaction.class;
        }
        if (clazz.equals(Bug.class)) {
            return DaoBug.class;
        }
        if (clazz.equals(Contribution.class)) {
            return DaoContribution.class;
        }
        if (clazz.equals(FileMetadata.class)) {
            return DaoFileMetadata.class;
        }
        if (clazz.equals(Kudos.class)) {
            return DaoKudos.class;
        }
        if (clazz.equals(Comment.class)) {
            return DaoComment.class;
        }
        if (clazz.equals(Feature.class)) {
            return DaoFeature.class;
        }
        if (clazz.equals(Offer.class)) {
            return DaoOffer.class;
        }
        if (clazz.equals(Translation.class)) {
            return DaoTranslation.class;
        }
        if (clazz.equals(Release.class)) {
            return DaoRelease.class;
        }
        if (clazz.equals(Kudosable.class) || clazz.equals(KudosableInterface.class)) {
            return DaoKudosable.class;
        }
        if (clazz.equals(UserContent.class) || clazz.equals(UserContentInterface.class)) {
            return DaoUserContent.class;
        }

        Log.model().error("Dao class not found for class: " + clazz.getCanonicalName());
        return null;
    }
}
