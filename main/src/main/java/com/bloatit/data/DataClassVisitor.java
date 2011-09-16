//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoMoneyWithdrawal dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoInvoice dao);
    ReturnType visit(DaoContributionInvoice dao);
    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoStringVersion dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoVersionedString dao);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoMilestoneContributionAmount daoMilestoneContributionAmount);

    /**
     * visit a persistent object. visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoNewsFeed daoNewsFeed);

    /**
     * visit a persistent object.
     * 
     * @param dao the visited dao
     * @return what you want
     */
    ReturnType visit(DaoExternalService daoExternalService);

    ReturnType visit(DaoExternalServiceMembership daoExternalServiceMembership);

    ReturnType visit(DaoFollow daoFollow);
}
