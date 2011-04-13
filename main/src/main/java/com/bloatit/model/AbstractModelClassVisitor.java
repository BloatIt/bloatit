package com.bloatit.model;

import org.apache.commons.lang.NotImplementedException;

public class AbstractModelClassVisitor<T> implements ModelClassVisitor<T> {

    @Override
    public T visit(final ExternalAccount model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final InternalAccount model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Member model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final BankTransaction model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Milestone model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Description model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Team model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final HighlightFeature model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final JoinTeamInvitation model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Software model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Transaction model) {

        throw new NotImplementedException();
    }

    @Override
    public T visit(final Bug model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Contribution model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final FileMetadata model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Kudos model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Comment model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Feature model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Offer model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Translation model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Release model) {
        throw new NotImplementedException();
    }
}
