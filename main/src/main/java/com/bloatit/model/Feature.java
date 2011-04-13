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
package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.WrongStateException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.context.User;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;

public interface Feature extends KudosableInterface<DaoFeature>, Commentable {

    /**
     * @param action is the type of action you can do on the property. (READ for
     *            the getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Comment</code> property.
     * @see #getComments()
     * @see #addComment(String)
     */
    boolean canAccessComment(final Action action);

    /**
     * @param action is the type of action you can do on the property. (READ for
     *            the getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Contribution</code> property.
     * @see #getContribution()
     * @see #getContributionMax()
     * @see #getContributionMin()
     * @see #getContributions()
     * @see #addContribution(BigDecimal, String)
     */
    boolean canAccessContribution(final Action action);

    /**
     * @param action is the type of action you can do on the property. (READ for
     *            the getter, WRITE for the SETTER etc.)
     * @return true if you can access the <code>Offer</code> property.
     * @see #getOffers()
     * @see #addOffer(Member, BigDecimal, String, Locale, Date, int)
     */
    boolean canAccessOffer(final Action action);

    /**
     * @return true if you can access the <code>Description</code> property.
     * @see #getDescription()
     */
    boolean canAccessDescription();

    /**
     * Add a contribution on this feature.
     * 
     * @param amount must be a positive non null value.
     * @param comment can be null or empty and should be less than 140 char
     *            long.
     * @throws NotEnoughMoneyException if the person logged does not have enough
     *             money to make this contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#WRITE} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    void addContribution(final BigDecimal amount, final String comment) throws NotEnoughMoneyException, UnauthorizedOperationException;

    /**
     * Add a new Offer on this Feature. You can do this operation when you are
     * in the {@link FeatureState#PENDING} or {@link FeatureState#PREPARING}
     * FeatureState. When you add the first Offer, the state pass from
     * {@link FeatureState#PENDING} to {@link FeatureState#PREPARING}; and this
     * offer is selected (see {@link DaoFeature#setSelectedOffer(DaoOffer)}).
     * The parameters of this function are used to create the first (non
     * optional) milestone in this offer.
     * 
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#WRITE} right on the <code>Offer</code>
     *             property.
     * @throws WrongStateException if the state is != from
     *             {@link FeatureState#PENDING} or
     *             {@link FeatureState#PREPARING}.
     * @see #authenticate(AuthToken)
     */
    Offer addOffer(Member author, BigDecimal amount, String description, Locale locale, Date expireDate, int secondsBeforeValidation)
            throws UnauthorizedOperationException;

    /**
     * For now only the admin can delete an offer.
     * 
     * @param offer is the offer to delete.
     * @throws UnauthorizedOperationException if the user does not has the
     *             <code>DELETED</code> right on the <code>Offer</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    void removeOffer(final Offer offer) throws UnauthorizedOperationException;

    /**
     * Works only in development state.
     * 
     * @throws UnauthorizedOperationException If this is not the current
     *             developer thats try to cancel the dev.
     */
    void cancelDevelopment() throws UnauthorizedOperationException;

    // TODO authorization
    boolean validateCurrentMilestone(final boolean force);

    /**
     * Used by Offer class. You should never have to use it
     * 
     * @param offer the offer to unselect. Nothing is done if the offer is not
     *            selected.
     */
    void unSelectOffer(final Offer offer);

    Date getValidationDate();

    /**
     * @return the first level comments on this feature.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Comment</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    PageIterable<Comment> getComments() throws UnauthorizedOperationException;

    /**
     * Get the total number of comments for this feature. It doesn't take into
     * account the pageSize if you are using paged list (cf:
     * {@link PageIterable}).
     * 
     * @return the total number of comments on this feature.
     */
    Long getCommentsCount();

    /**
     * @return all the Contributions on this Feature.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    PageIterable<Contribution> getContributions() throws UnauthorizedOperationException;

    /**
     * Return the progression in percent. It compare the amount of contribution
     * to the amount of the current offer.
     * 
     * @return a percentage. It can be > 100 if the amount of contributions is
     *         greater than the amount for the current offer. If the offer
     *         amount is 0 then it return Float.POSITIVE_INFINITY.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    float getProgression() throws UnauthorizedOperationException;

    /**
     * Return the progression due by the member in percent. It compare the
     * amount of contribution to the amount of the current offer.
     * 
     * @return a percentage. It can be > 100 if the amount of contributions is
     *         greater than the amount for the current offer. If the offer
     *         amount is 0 then it return Float.POSITIVE_INFINITY.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    float getMemberProgression(User member) throws UnauthorizedOperationException;

    /**
     * Return the progression due by the amount in percent. It compare the
     * amount of contribution to the amount of the current offer.
     * 
     * @return a percentage. It can be > 100 if the amount of contributions is
     *         greater than the amount for the current offer. If the offer
     *         amount is 0 then it return Float.POSITIVE_INFINITY.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    float getRelativeProgression(BigDecimal amount) throws UnauthorizedOperationException;

    /**
     * @return return the sum of the values of all the contributions on this
     *         feature.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    BigDecimal getContribution() throws UnauthorizedOperationException;

    /**
     * @return return the value of the contribution with the max contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    BigDecimal getContributionMax() throws UnauthorizedOperationException;

    /**
     * @return return the value of the contribution with the min contribution.
     * @throws UnauthorizedOperationException if the user does not has the
     *             {@link Action#READ} right on the <code>Contribution</code>
     *             property.
     * @see #authenticate(AuthToken)
     */
    BigDecimal getContributionMin() throws UnauthorizedOperationException;

    /**
     * @return the current Description of this feature.
     * @throws UnauthorizedOperationException if the user does not has the right
     *             on the <code>Description</code> property.
     * @see #authenticate(AuthToken)
     */
    Description getDescription() throws UnauthorizedOperationException;

    /**
     * @return the current associate software of this feature.
     * @throws UnauthorizedOperationException if the user does not has the right
     *             on the <code>Software</code> property.
     * @see #authenticate(AuthToken)
     */
    Software getSoftware() throws UnauthorizedOperationException;

    /**
     * @return all the offers on this feature.
     * @throws UnauthorizedOperationException if the user does not has the
     *             <code>READ</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    PageIterable<Offer> getOffers() throws UnauthorizedOperationException;

    /**
     * The current offer is the offer with the max popularity then the min
     * amount.
     * 
     * @return the current offer for this feature, or null if there is no offer.
     * @throws UnauthorizedOperationException if the user does not has the
     *             <code>READ</code> right on the <code>Offer</code> property.
     * @see #authenticate(AuthToken)
     */
    Offer getSelectedOffer() throws UnauthorizedOperationException;

    /**
     * A validated offer is an offer selected for more than one day. (If you are
     * in {@link FeatureState#DEVELOPPING} state then there should be always a
     * validated offer.
     * 
     * @return the validated offer or null if there is no valid offer.
     * @throws UnauthorizedOperationException if you do not have the
     *             <code>READ</code> right on the offer property
     */
    Offer getValidatedOffer() throws UnauthorizedOperationException;

    /**
     * @throws UnauthorizedOperationException if the user does not has the
     *             <code>READ</code> right on the <code>Description</code>
     *             property.
     * @see #authenticate(AuthToken)
     * @see #getDescription()
     */
    String getTitle() throws UnauthorizedOperationException;

    FeatureState getFeatureState();

    int countOpenBugs();

    PageIterable<Bug> getOpenBugs();

    PageIterable<Bug> getClosedBugs();

    void updateDevelopmentState();

    void computeSelectedOffer() throws UnauthorizedOperationException;

    void setFeatureState(FeatureState featureState) throws UnauthorizedOperationException;

}
