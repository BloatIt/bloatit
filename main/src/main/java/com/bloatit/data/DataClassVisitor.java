package com.bloatit.data;

/**
 * This is the interface that all the visitors from the Data layer have to
 * implement.
 * 
 * @author Thomas Guyard
 * @param <ReturnType> the return type of the visit method
 */
public interface DataClassVisitor<ReturnType> {

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoExternalAccount dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoInternalAccount dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoMember dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoBankTransaction dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoMilestone dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoDescription dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoTeam dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoHighlightFeature dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoImage dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoJoinTeamInvitation dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoSoftware dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoTransaction dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoBug dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */

    ReturnType visit(DaoContribution dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoFileMetadata dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoKudos dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoComment dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoFeature dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoOffer dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoTranslation dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoRelease dao);

}
