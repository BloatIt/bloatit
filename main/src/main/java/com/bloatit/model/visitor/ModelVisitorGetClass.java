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
package com.bloatit.model.visitor;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoContributionInvoice;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoEvent;
import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoExternalService;
import com.bloatit.data.DaoExternalServiceMembership;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFollow;
import com.bloatit.data.DaoFollowActor;
import com.bloatit.data.DaoFollowFeature;
import com.bloatit.data.DaoFollowSoftware;
import com.bloatit.data.DaoHighlightFeature;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoInvoice;
import com.bloatit.data.DaoJoinTeamInvitation;
import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.data.DaoNewsFeed;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.DaoTranslation;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.ContributionInvoice;
import com.bloatit.model.Description;
import com.bloatit.model.Event;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.ExternalService;
import com.bloatit.model.ExternalServiceMembership;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Follow;
import com.bloatit.model.FollowActor;
import com.bloatit.model.FollowFeature;
import com.bloatit.model.FollowSoftware;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Invoice;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.MilestoneContributionAmount;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.Transaction;
import com.bloatit.model.Translation;

/**
 * The Class ModelVisitorGetClass.
 */
public class ModelVisitorGetClass implements ModelClassVisitor<Class<?>> {

    @Override
    public Class<?> visit(final ExternalAccount model) {
        return DaoExternalAccount.class;
    }

    @Override
    public Class<?> visit(final InternalAccount model) {
        return DaoInternalAccount.class;
    }

    @Override
    public Class<?> visit(final Member model) {
        return DaoMember.class;
    }

    @Override
    public Class<?> visit(final BankTransaction model) {
        return DaoBankTransaction.class;
    }

    @Override
    public Class<?> visit(final Milestone model) {
        return DaoMilestone.class;
    }

    @Override
    public Class<?> visit(final Description model) {
        return DaoDescription.class;
    }

    @Override
    public Class<?> visit(final Team model) {
        return DaoTeam.class;
    }

    @Override
    public Class<?> visit(final HighlightFeature model) {
        return DaoHighlightFeature.class;
    }

    @Override
    public Class<?> visit(final JoinTeamInvitation model) {
        return DaoJoinTeamInvitation.class;
    }

    @Override
    public Class<?> visit(final Software model) {
        return DaoSoftware.class;
    }

    @Override
    public Class<?> visit(final Transaction model) {
        return DaoTransaction.class;
    }

    @Override
    public Class<?> visit(final Bug model) {
        return DaoBug.class;
    }

    @Override
    public Class<?> visit(final Contribution model) {
        return DaoContribution.class;
    }

    @Override
    public Class<?> visit(final FileMetadata model) {
        return DaoFileMetadata.class;
    }

    @Override
    public Class<?> visit(final Kudos model) {
        return DaoKudos.class;
    }

    @Override
    public Class<?> visit(final Comment model) {
        return DaoComment.class;
    }

    @Override
    public Class<?> visit(final Feature model) {
        return DaoFeature.class;
    }

    @Override
    public Class<?> visit(final Offer model) {
        return DaoOffer.class;
    }

    @Override
    public Class<?> visit(final Translation model) {
        return DaoTranslation.class;
    }

    @Override
    public Class<?> visit(final Release model) {
        return DaoRelease.class;
    }

    @Override
    public Class<?> visit(final MoneyWithdrawal model) {
        return DaoMoneyWithdrawal.class;
    }

    @Override
    public Class<?> visit(final Invoice model) {
        return DaoInvoice.class;
    }

    @Override
    public Class<?> visit(final ContributionInvoice model) {
        return DaoContributionInvoice.class;
    }

    @Override
    public Class<?> visit(final MilestoneContributionAmount model) {
        return MilestoneContributionAmount.class;
    }

    @Override
    public Class<?> visit(final NewsFeed newsFeed) {
        return DaoNewsFeed.class;
    }

    @Override
    public Class<?> visit(final ExternalService externalService) {
        return DaoExternalService.class;
    }

    @Override
    public Class<?> visit(ExternalServiceMembership externalService) {
        return DaoExternalServiceMembership.class;
    }

    @Override
    public Class<?> visit(Follow model) {
        return DaoFollow.class;
    }

    @Override
    public Class<?> visit(Event event) {
        return DaoEvent.class;
    }

    @Override
    public Class<?> visit(FollowFeature model) {
        return DaoFollowFeature.class;
    }

    @Override
    public Class<?> visit(FollowSoftware model) {
        return DaoFollowSoftware.class;
    }

    @Override
    public Class<?> visit(FollowActor model) {
        return DaoFollowActor.class;
    }
}
