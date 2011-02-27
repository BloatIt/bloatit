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
import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.data.DaoJoinGroupInvitation;
import com.bloatit.data.DaoKudos;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoProject;
import com.bloatit.data.DaoRelease;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.DaoTranslation;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.data.queries.DBRequests;

public class GenericConstructor {

    public static IdentifiableInterface create(Class<? extends IdentifiableInterface> clazz, Integer id) {
        return ((DaoIdentifiable) DBRequests.getById(getDaoClass(clazz), id)).accept(new DataVisitorConstructor());
    }

    public static Class<?> getDaoClass(Class<? extends IdentifiableInterface> clazz) {
        if (clazz.equals(ExternalAccount.class)) {
            return DaoExternalAccount.class;
        }
        if (clazz.equals(InternalAccount.class)) {
            return DaoInternalAccount.class;
        }
        if (clazz.equals(Member.class)) {
            return DaoMember.class;
        }
        if (clazz.equals(BankTransaction.class)) {
            return DaoBankTransaction.class;
        }
        if (clazz.equals(Batch.class)) {
            return DaoBatch.class;
        }
        if (clazz.equals(Description.class)) {
            return DaoDescription.class;
        }
        if (clazz.equals(Group.class)) {
            return DaoGroup.class;
        }
        if (clazz.equals(HighlightDemand.class)) {
            return DaoHighlightDemand.class;
        }
        if (clazz.equals(JoinGroupInvitation.class)) {
            return DaoJoinGroupInvitation.class;
        }
        if (clazz.equals(Project.class)) {
            return DaoProject.class;
        }
        if (clazz.equals(Transaction.class)) {
            return DaoTransaction.class;
        }
        if (clazz.equals(Bug.class)) {
            return DaoBug.class;
        }
        if (clazz.equals(Contribution.class)) {
            return DaoContribution.class;
        }
        if (clazz.equals(FileMetadata.class)) {
            return DaoFileMetadata.class;
        }
        if (clazz.equals(Kudos.class)) {
            return DaoKudos.class;
        }
        if (clazz.equals(Comment.class)) {
            return DaoComment.class;
        }
        if (clazz.equals(Demand.class)) {
            return DaoDemand.class;
        }
        if (clazz.equals(Offer.class)) {
            return DaoOffer.class;
        }
        if (clazz.equals(Translation.class)) {
            return DaoTranslation.class;
        }
        if (clazz.equals(Release.class)) {
            return DaoRelease.class;
        }
        return null;
    }
}
