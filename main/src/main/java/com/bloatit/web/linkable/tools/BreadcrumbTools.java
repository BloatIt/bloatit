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
package com.bloatit.web.linkable.tools;

import org.apache.commons.lang.NotImplementedException;

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
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.visitor.ModelClassVisitor;
import com.bloatit.web.linkable.bugs.BugPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.members.MemberPage;
import com.bloatit.web.linkable.release.ReleasePage;
import com.bloatit.web.linkable.softwares.SoftwarePage;
import com.bloatit.web.linkable.team.TeamPage;

public class BreadcrumbTools {

    public static Breadcrumb generateBreadcrumb(final UserContentInterface userContent) {

        return userContent.accept(new ModelClassVisitor<Breadcrumb>() {

            @Override
            public Breadcrumb visit(final ExternalAccount model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final InternalAccount model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Member model) {
                return MemberPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final BankTransaction model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Milestone model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Description model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Team model) {
                return TeamPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final HighlightFeature model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final JoinTeamInvitation model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Software model) {
                return SoftwarePage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final Transaction model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Bug model) {
                return BugPage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final Contribution model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final FileMetadata model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Kudos model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Comment model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Feature model) {
                return FeaturePage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final Offer model) {
                return FeaturePage.generateBreadcrumbOffers(model.getFeature());
            }

            @Override
            public Breadcrumb visit(final Translation model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Release model) {
                return ReleasePage.generateBreadcrumb(model);
            }

            @Override
            public Breadcrumb visit(final MoneyWithdrawal model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Invoice model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final ContributionInvoice model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final MilestoneContributionAmount model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final NewsFeed newsFeed) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final ExternalService externalService) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final ExternalServiceMembership externalService) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Follow model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final Event event) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final FollowFeature model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final FollowSoftware model) {
                throw new NotImplementedException();
            }

            @Override
            public Breadcrumb visit(final FollowActor model) {
                throw new NotImplementedException();
            }
        });
    }

}
