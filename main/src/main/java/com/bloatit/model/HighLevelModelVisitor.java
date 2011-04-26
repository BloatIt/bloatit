package com.bloatit.model;

public abstract class HighLevelModelVisitor<T> implements ModelClassVisitor<T> {

    public abstract T visitAbstract(Account<?> model);

    public abstract T visitAbstract(Actor<?> model);

    public abstract T visitAbstract(UserContentInterface model);

    public abstract T visitAbstract(BankTransaction model);

    public abstract T visitAbstract(Milestone model);

    public abstract T visitAbstract(Description model);

    public abstract T visitAbstract(HighlightFeature model);

    public abstract T visitAbstract(JoinTeamInvitation model);

    public abstract T visitAbstract(Software model);

    public abstract T visitAbstract(Transaction model);

    @Override
    public final T visit(final ExternalAccount model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final InternalAccount model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Member model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final BankTransaction model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Milestone model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Description model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Team model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final HighlightFeature model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final JoinTeamInvitation model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Software model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Transaction model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Bug model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Contribution model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final FileMetadata model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Kudos model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Comment model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Feature model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Offer model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Translation model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Release model) {
        return visitAbstract(model);
    }
}
