package com.bloatit.rest.utils;

import com.bloatit.framework.utils.Image;
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

public class ModelToRestVisitor implements ModelClassVisitor<RestElement> {

    @Override
    public RestElement visit(ExternalAccount model) {
        return null;
    }

    @Override
    public RestElement visit(InternalAccount model) {
        return null;
    }

    @Override
    public RestElement visit(Member model) {
        return null;
    }

    @Override
    public RestElement visit(BankTransaction model) {
        return null;
    }

    @Override
    public RestElement visit(Batch model) {
        return null;
    }

    @Override
    public RestElement visit(Description model) {
        return null;
    }

    @Override
    public RestElement visit(Group model) {
        return null;
    }

    @Override
    public RestElement visit(HighlightDemand model) {
        return null;
    }

    @Override
    public RestElement visit(Image model) {
        return null;
    }

    @Override
    public RestElement visit(JoinGroupInvitation model) {
        return null;
    }

    @Override
    public RestElement visit(Project model) {
        return null;
    }

    @Override
    public RestElement visit(Transaction model) {
        return null;
    }

    @Override
    public RestElement visit(Bug model) {
        return null;
    }

    @Override
    public RestElement visit(Contribution model) {
        return null;
    }

    @Override
    public RestElement visit(FileMetadata model) {
        return null;
    }

    @Override
    public RestElement visit(Kudos model) {
        return null;
    }

    @Override
    public RestElement visit(Comment model) {
        return null;
    }

    @Override
    public RestElement visit(Demand model) {
        return null;
    }

    @Override
    public RestElement visit(Offer model) {
        return null;
    }

    @Override
    public RestElement visit(Translation model) {
        return null;
    }

    @Override
    public RestElement visit(Release model) {
        return null;
    }

}
