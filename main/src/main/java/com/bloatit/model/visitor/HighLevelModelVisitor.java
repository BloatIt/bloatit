package com.bloatit.model.visitor;

import com.bloatit.model.Account;
import com.bloatit.model.Actor;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.ExternalService;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.InternalAccount;
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
import com.bloatit.model.UserContentInterface;

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

    public abstract T visitAbstract(MoneyWithdrawal model);

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
    public final T visit(final ExternalService model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final Release model) {
        return visitAbstract(model);
    }

    @Override
    public final T visit(final MoneyWithdrawal model) {
        return visitAbstract(model);
    }
}
