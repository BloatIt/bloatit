/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.model.visitor;

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

public interface ModelClassVisitor<ReturnType> {

    ReturnType visit(ExternalAccount model);

    ReturnType visit(InternalAccount model);

    ReturnType visit(Member model);

    ReturnType visit(BankTransaction model);

    ReturnType visit(Milestone model);

    ReturnType visit(Description model);

    ReturnType visit(Team model);

    ReturnType visit(HighlightFeature model);

    ReturnType visit(JoinTeamInvitation model);

    ReturnType visit(Software model);

    ReturnType visit(Transaction model);

    ReturnType visit(Bug model);

    ReturnType visit(Contribution model);

    ReturnType visit(FileMetadata model);

    ReturnType visit(Kudos model);

    ReturnType visit(Comment model);

    ReturnType visit(Feature model);

    ReturnType visit(Offer model);

    ReturnType visit(Translation model);

    ReturnType visit(Release model);

    ReturnType visit(MoneyWithdrawal model);

    ReturnType visit(Invoice model);

    ReturnType visit(ContributionInvoice model);

    ReturnType visit(MilestoneContributionAmount model);

    ReturnType visit(NewsFeed newsFeed);

    ReturnType visit(ExternalService externalService);
    
    ReturnType visit(ExternalServiceMembership externalService);

    ReturnType visit(Follow model);

    ReturnType visit(Event event);
    
    ReturnType visit(FollowFeature model);
    
    ReturnType visit(FollowSoftware model);
    
    ReturnType visit(FollowActor model);
}
