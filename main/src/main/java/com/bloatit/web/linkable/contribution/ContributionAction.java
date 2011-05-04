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
package com.bloatit.web.linkable.contribution;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributionActionUrl;
import com.bloatit.web.url.ContributionProcessUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A response to a form used to create a contribution to a feature
 */
@ParamContainer("action/contribute")
public final class ContributionAction extends UserContentAction {

    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @RequestParam
    private final ContributionProcess process;

    // Keep it for consistency
    @SuppressWarnings("unused")
    private final ContributionActionUrl url;

    public ContributionAction(final ContributionActionUrl url) {
        super(url, url.getProcess().getTeam(), UserTeamRight.BANK);
        this.url = url;
        this.process = url.getProcess();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        try {
            process.getFeature().addContribution(process.getAmount(), process.getComment());
            session.notifyGood(Context.tr("Thanks you for crediting {0} on this feature.", Context.getLocalizator()
                                                                                                  .getCurrency(process.getAmount())
                                                                                                  .getSimpleEuroString()));

            final FeaturePageUrl featurePageUrl = new FeaturePageUrl(process.getFeature());
            featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.CONTRIBUTIONS_TAB);
            process.close();
            return featurePageUrl;
        } catch (final NotEnoughMoneyException e) {
            session.notifyBad(Context.tr("You need to charge your account before you can contribute."));
            return new CheckContributionPageUrl(process);
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to contribute on this feature."));
            return new ContributionProcessUrl(process.getFeature());
        } catch (final RuntimeException e) {
            process.close();
            throw e;
        }
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        // Add a can access contribution..
        return NO_ERROR;
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return true;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken userToken) {
        return new CheckContributionPageUrl(process);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to contribute.");
    }

    @Override
    protected void doTransmitParameters() {
        // No parameters to transmit.
    }

}
