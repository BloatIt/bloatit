/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import java.math.BigDecimal;

import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.url.AccountChargingPageUrl;
import com.bloatit.web.url.ContributePageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A response to a form used to create a contribution to a feature
 */
@ParamContainer("action/contribute")
public final class ContributionAction extends LoggedAction {

    public static final String AMOUNT_CODE = "contributionAmount";
    public static final String COMMENT_CODE = "comment";
    public static final String TARGET_FEATURE = "targetFeature";

    @RequestParam(name = TARGET_FEATURE)
    private final Feature targetFeature;

    @RequestParam(name = COMMENT_CODE, role = Role.POST)
    @ParamConstraint(max = "140", maxErrorMsg = @tr("Your comment is too long. It must be less than 140 char long."))
    @Optional
    private final String comment;

    @RequestParam(name = AMOUNT_CODE, role = Role.POST)
    @ParamConstraint(min = "0", minIsExclusive = true, minErrorMsg = @tr("Amount must be superior to 0."),//
    max = "1000000000", maxErrorMsg = @tr("We cannot accept such a generous offer!"),//
    precision = 0, precisionErrorMsg = @tr("Please do not use Cents."))
    private final BigDecimal amount;

    private final ContributionActionUrl url;

    public ContributionAction(final ContributionActionUrl url) {
        super(url);
        this.url = url;
        this.targetFeature = url.getTargetFeature();
        this.comment = url.getComment();
        this.amount = url.getAmount();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        try {
            targetFeature.addContribution(amount, comment);
            session.notifyGood(Context.tr("Thanks you for crediting {0} on this feature", Context.getLocalizator().getCurrency(amount).getLocaleString()));
            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(targetFeature);
            featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.PARTICIPATIONS_TAB);
            return featurePageUrl;
        } catch (final NotEnoughMoneyException e) {
            session.notifyBad(Context.tr("You need to charge your account before you can contribute."));
            session.addParameter(url.getAmountParameter());
            session.addParameter(url.getCommentParameter());

            session.setTargetPage(this.url);
            return new AccountChargingPageUrl();
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to contribute on this feature."));
            return new ContributePageUrl(targetFeature);
        }
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
        session.addParameter(url.getCommentParameter());
        session.addParameter(url.getAmountParameter());

        return new ContributePageUrl(targetFeature);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to contribute.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getCommentParameter());
        session.addParameter(url.getAmountParameter());
    }
}
