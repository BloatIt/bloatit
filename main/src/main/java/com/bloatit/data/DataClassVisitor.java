package com.bloatit.data;

public interface DataClassVisitor<ReturnType> {

    ReturnType visit(DaoExternalAccount dao);

    ReturnType visit(DaoInternalAccount dao);

    ReturnType visit(DaoMember dao);

    ReturnType visit(DaoBankTransaction dao);

    ReturnType visit(DaoBatch dao);

    ReturnType visit(DaoDescription dao);

    ReturnType visit(DaoGroup dao);

    ReturnType visit(DaoHighlightDemand dao);

    ReturnType visit(DaoImage dao);

    ReturnType visit(DaoJoinGroupInvitation dao);

    ReturnType visit(DaoProject dao);

    ReturnType visit(DaoTransaction dao);

    ReturnType visit(DaoBug dao);

    ReturnType visit(DaoContribution dao);

    ReturnType visit(DaoFileMetadata dao);

    ReturnType visit(DaoKudos dao);

    ReturnType visit(DaoComment dao);

    ReturnType visit(DaoFeature dao);

    ReturnType visit(DaoOffer dao);

    ReturnType visit(DaoTranslation dao);

    ReturnType visit(DaoRelease dao);

}
