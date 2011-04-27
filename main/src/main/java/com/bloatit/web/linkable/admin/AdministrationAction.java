package com.bloatit.web.linkable.admin;

import java.util.List;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.url.Loaders;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.UserContent;
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
    @ParamConstraint
    @Optional
    private final DisplayableState stateToSet;

    @SuppressWarnings("unused")
    private final AdministrationActionUrl url;

    @SuppressWarnings("unchecked")
    public AdministrationAction(final AdministrationActionUrl url) {
        super(url);
        this.url = url;
        contents = url.getContents();
        action = url.getAction();
        stateToSet = url.getStateToSet();
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
                        session.notifyBad("SetValidationDate not implemented yet.");
                        break;
                    case UPDATE_DEVELOPMENT_STATE:
                        Loaders.fromStr(Feature.class, id).updateDevelopmentState();
                        break;
                    case COMPUTE_SELECTED_OFFER:
                        Loaders.fromStr(Feature.class, id).computeSelectedOffer();
                        break;
                    case SET_FEATURE_IN_DEVELOPMENT:
                        final Feature feature = Loaders.fromStr(Feature.class, id);
                        if (feature.getSelectedOffer() == null || feature.getSelectedOffer().getAmount().compareTo(feature.getContribution()) > 0) {
                            session.notifyBad("There is no offer or not enough money. So no developement state for id: " + feature.getId() + ".");
                        } else {
                            feature.setFeatureState(FeatureState.DEVELOPPING);
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
    protected Url doProcessErrors() {
        return session.pickPreferredPage();
    }

    @Override
    protected void transmitParameters() {
        // TODO redo this page
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
    }
}
