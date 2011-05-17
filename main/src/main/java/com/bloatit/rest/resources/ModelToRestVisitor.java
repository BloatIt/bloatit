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
package com.bloatit.rest.resources;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.restprocessor.RestElement;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.Invoice;
import com.bloatit.model.InvoicingContact;
import com.bloatit.model.JoinTeamInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.ModelClassVisitor;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.Transaction;
import com.bloatit.model.Translation;

public class ModelToRestVisitor implements ModelClassVisitor<RestElement<?>> {

    @Override
    public RestElement<ExternalAccount> visit(final ExternalAccount model) {
        return new RestExternalAccount(model);
    }

    @Override
    public RestElement<InternalAccount> visit(final InternalAccount model) {
        return new RestInternalAccount(model);
    }

    @Override
    public RestElement<Member> visit(final Member model) {
        return new RestMember(model);
    }

    @Override
    public RestElement<BankTransaction> visit(final BankTransaction model) {
        return new RestBankTransaction(model);
    }

    @Override
    public RestElement<Milestone> visit(final Milestone model) {
        return new RestMilestone(model);
    }

    @Override
    public RestElement<Description> visit(final Description model) {
        return new RestDescription(model);
    }

    @Override
    public RestElement<Team> visit(final Team model) {
        return new RestTeam(model);
    }

    @Override
    public RestElement<HighlightFeature> visit(final HighlightFeature model) {
        return new RestHighlightFeature(model);
    }

    @Override
    public RestElement<JoinTeamInvitation> visit(final JoinTeamInvitation model) {
        return new RestJoinTeamInvitation(model);
    }

    @Override
    public RestElement<Software> visit(final Software model) {
        return new RestSoftware(model);
    }

    @Override
    public RestElement<Transaction> visit(final Transaction model) {
        return new RestTransaction(model);
    }

    @Override
    public RestElement<Bug> visit(final Bug model) {
        return new RestBug(model);
    }

    @Override
    public RestElement<Contribution> visit(final Contribution model) {
        return new RestContribution(model);
    }

    @Override
    public RestElement<FileMetadata> visit(final FileMetadata model) {
        return new RestFileMetadata(model);
    }

    @Override
    public RestElement<Kudos> visit(final Kudos model) {
        return new RestKudos(model);
    }

    @Override
    public RestElement<Comment> visit(final Comment model) {
        return new RestComment(model);
    }

    @Override
    public RestElement<Feature> visit(final Feature model) {
        return new RestFeature(model);
    }

    @Override
    public RestElement<Offer> visit(final Offer model) {
        return new RestOffer(model);
    }

    @Override
    public RestElement<Translation> visit(final Translation model) {
        return new RestTranslation(model);
    }

    @Override
    public RestElement<Release> visit(final Release model) {
        return new RestRelease(model);
    }

    @Override
    public RestElement<?> visit(MoneyWithdrawal moneyWithdrawal) {
        throw new NotImplementedException();
    }

    @Override
    public RestElement<?> visit(Invoice invoice) {
        throw new NotImplementedException();
    }

    @Override
    public RestElement<?> visit(InvoicingContact invoicingContact) {
        throw new NotImplementedException();
    }
}
