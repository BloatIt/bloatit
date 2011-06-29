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
package com.bloatit.web.linkable.admin;

import java.util.List;

import com.bloatit.common.Log;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.Loaders;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Feature;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.UserContent;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.LoginPageUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends AdminAction {

    protected static final String POPULARITY_STATE_CODE = "popularitystate";
    protected static final String FEATURE_STATE_CODE = "featurestate";

    @RequestParam(name = "id", role = Role.POST)
    private final List<Integer> contents;

    @RequestParam(name = "action", role = Role.POST)
    private final AdminActionManager.Action action;

    @RequestParam(name = POPULARITY_STATE_CODE, role = Role.POST)
    @Optional
    private final DisplayableState stateToSet;

    @RequestParam(name = FEATURE_STATE_CODE, role = Role.POST)
    @Optional
    private final DisplayableFeatureState featureStateToSet;

    @SuppressWarnings("unused")
    private final AdministrationActionUrl url;

    @SuppressWarnings("unchecked")
    public AdministrationAction(final AdministrationActionUrl url) {
        super(url);
        this.url = url;
        contents = url.getContents();
        action = url.getAction();
        stateToSet = url.getStateToSet();
        featureStateToSet = url.getFeatureStateToSet();
    }

    @Override
    public Url doProcessAdmin() {
        try {
            for (final Integer content : contents) {
                final String id = String.valueOf(content);

                switch (action) {
                    case DELETE:
                        Loaders.fromStr(UserContent.class, id).delete();
                        break;
                    case RESTORE:
                        Loaders.fromStr(UserContent.class, id).restore();
                        break;
                    case LOCK:
                        Loaders.fromStr(Kudosable.class, id).lockPopularity();
                        break;
                    case UNLOCK:
                        Loaders.fromStr(Kudosable.class, id).unlockPopularity();
                        break;
                    case SETSTATE:
                        if (stateToSet != null && stateToSet != DisplayableState.NO_FILTER) {
                            Loaders.fromStr(Kudosable.class, id).setState(DisplayableState.getState(stateToSet));
                        }
                        break;
                    case SET_VALIDATION_DATE:
                        session.notifyWarning("SetValidationDate not implemented yet.");
                        break;
                    case UPDATE_DEVELOPMENT_STATE:
                        Loaders.fromStr(Feature.class, id).updateDevelopmentState();
                        break;
                    case COMPUTE_SELECTED_OFFER:
                        Loaders.fromStr(Feature.class, id).computeSelectedOffer();
                        break;
                    case SET_FEATURE_IN_DEVELOPMENT:
                        final Feature feature = Loaders.fromStr(Feature.class, id);
                        switch (featureStateToSet) {
                            case DEVELOPPING:
                                if (feature.getSelectedOffer() == null
                                        || feature.getSelectedOffer().getAmount().compareTo(feature.getContribution()) > 0) {
                                    session.notifyWarning("There is no offer or not enough money. So no development state for id: " + feature.getId()
                                            + ".");
                                } else {
                                    feature.setFeatureState(FeatureState.DEVELOPPING);
                                }
                                break;
                            case DISCARDED:
                                feature.setFeatureState(FeatureState.DISCARDED);
                                break;
                            case FINISHED:
                                feature.setFeatureState(FeatureState.FINISHED);
                                break;
                            case PENDING:
                            case PREPARING:
                            case NO_FILTER:
                            default:
                                Log.web().info("Wrong feature state. Nothing to do.");
                                session.notifyWarning("Wrong feature state. Nothing to do.");
                                break;
                        }
                        break;
                    case VALIDATE_BATCH:
                        Loaders.fromStr(Milestone.class, id).validate();
                        break;
                    case FORCE_VALIDATE_BATCH:
                        Loaders.fromStr(Milestone.class, id).forceValidate();
                        break;
                    default:
                        break;
                }
            }
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(getRefusalReason());
            return new LoginPageUrl();
        } catch (final ConversionErrorException e) {
            session.notifyError(e.getMessage());
        }
        return session.pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        return session.pickPreferredPage();
    }

    @Override
    protected void transmitParameters() {
        // Ok your are an admin. do it yourself...
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }
}
