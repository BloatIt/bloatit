package com.bloatit.rest.resources;

import com.bloatit.model.BankTransaction;
import com.bloatit.model.Batch;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Demand;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.Release;
import com.bloatit.model.Transaction;
import com.bloatit.model.Translation;
import com.bloatit.rest.RestElement;

public class ModelToRestVisitor implements ModelClassVisitor<RestElement<?>> {

    @Override
    public RestElement<ExternalAccount> visit(ExternalAccount model) {
        return new RestExternalAccount(model);
    }

    @Override
    public RestElement<InternalAccount> visit(InternalAccount model) {
        return new RestInternalAccount(model);
    }

    @Override
    public RestElement<Member> visit(Member model) {
        return new RestMember(model);
    }

    @Override
    public RestElement<BankTransaction> visit(BankTransaction model) {
        return new RestBankTransaction(model);
    }

    @Override
    public RestElement<Batch> visit(Batch model) {
        return new RestBatch(model);
    }

    @Override
    public RestElement<Description> visit(Description model) {
        return new RestDescription(model);
    }

    @Override
    public RestElement<Group> visit(Group model) {
        return new RestGroup(model);
    }

    @Override
    public RestElement<HighlightDemand> visit(HighlightDemand model) {
        return new RestHighlightDemand(model);
    }

    @Override
    public RestElement<JoinGroupInvitation> visit(JoinGroupInvitation model) {
        return new RestJoinGroupInvitation(model);
    }

    @Override
    public RestElement<Project> visit(Project model) {
        return new RestProject(model);
    }

    @Override
    public RestElement<Transaction> visit(Transaction model) {
        return new RestTransaction(model);
    }

    @Override
    public RestElement<Bug> visit(Bug model) {
        return new RestBug(model);
    }

    @Override
    public RestElement<Contribution> visit(Contribution model) {
        return new RestContribution(model);
    }

    @Override
    public RestElement<FileMetadata> visit(FileMetadata model) {
        return new RestFileMetadata(model);
    }

    @Override
    public RestElement<Kudos> visit(Kudos model) {
        return new RestKudos(model);
    }

    @Override
    public RestElement<Comment> visit(Comment model) {
        return new RestComment(model);
    }

    @Override
    public RestElement<Demand> visit(Demand model) {
        return new RestDemand(model);
    }

    @Override
    public RestElement<Offer> visit(Offer model) {
        return new RestOffer(model);
    }

    @Override
    public RestElement<Translation> visit(Translation model) {
        return new RestTranslation(model);
    }

    @Override
    public RestElement<Release> visit(Release model) {
        return new RestRelease(model);
    }
}
