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

import com.bloatit.framework.rest.RestElement;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Batch;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Feature;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.ModelClassVisitor;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.Release;
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
    public RestElement<Batch> visit(final Batch model) {
        return new RestBatch(model);
    }

    @Override
    public RestElement<Description> visit(final Description model) {
        return new RestDescription(model);
    }

    @Override
    public RestElement<Group> visit(final Group model) {
        return new RestGroup(model);
    }

    @Override
    public RestElement<HighlightDemand> visit(final HighlightDemand model) {
        return new RestHighlightDemand(model);
    }

    @Override
    public RestElement<JoinGroupInvitation> visit(final JoinGroupInvitation model) {
        return new RestJoinGroupInvitation(model);
    }

    @Override
    public RestElement<Project> visit(final Project model) {
        return new RestProject(model);
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
        return new RestDemand(model);
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
}
