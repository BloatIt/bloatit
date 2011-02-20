package com.bloatit.model;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBatch;
import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoHighlightDemand;
import com.bloatit.data.DaoImage;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoProject;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.DaoTranslation;
import com.bloatit.data.DataClassVisitor;
import com.bloatit.model.demand.DemandImplementation;

public class ConstructorVisitor implements DataClassVisitor<Identifiable<?>> {

    @Override
    public Identifiable<?> visit(DaoExternalAccount dao) {
        return ExternalAccount.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoInternalAccount dao) {
        return InternalAccount.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoMember dao) {
        return Member.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoBankTransaction dao) {
        return BankTransaction.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoBatch dao) {
        return Batch.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoDescription dao) {
        return Description.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoGroup dao) {
        return Group.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoHighlightDemand dao) {
        return HighlightDemand.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoImage dao) {
        return null;
    }

    @Override
    public Identifiable<?> visit(DaoJoinGroupInvitation dao) {
        return JoinGroupInvitation.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoProject dao) {
        return Project.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoTransaction dao) {
        return Transaction.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoBug dao) {
        return Bug.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoContribution dao) {
        return Contribution.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoFileMetadata dao) {
        return FileMetadata.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoKudos dao) {
        return Kudos.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoComment dao) {
        return Comment.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoDemand dao) {
        return DemandImplementation.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoOffer dao) {
        return Offer.create(dao);
    }

    @Override
    public Identifiable<?> visit(DaoTranslation dao) {
        return Translation.create(dao);
    }

}
