package com.bloatit.web.actions;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.List;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMember;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Batch;
import com.bloatit.model.Feature;
import com.bloatit.model.Identifiable;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Member;
import com.bloatit.model.UserContent;
import com.bloatit.web.pages.admin.AdminActionManager;
import com.bloatit.web.pages.admin.DisplayableState;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.LoginPageUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends LoggedAction {

    public static final String POPULARITY_STATE_CODE = "popularitystate";
    public static final String FEATURE_STATE_CODE = "featurestate";

    @RequestParam(name = "id", role = Role.POST)
    private final List<Identifiable> contents;

    @RequestParam(name = "action", role = Role.POST)
    private final AdminActionManager.Action action;

    @RequestParam(name = POPULARITY_STATE_CODE, role = Role.POST)
    @ParamConstraint
    @Optional
    private final DisplayableState stateToSet;

    private final AdministrationActionUrl url;

    public AdministrationAction(final AdministrationActionUrl url) {
        super(url);
        this.url = url;
        contents = url.getContents();
        action = url.getAction();
        stateToSet = url.getStateToSet();
    }

    @Override
    public Url doProcessRestricted(Member authenticatedMember) {
        if (!authenticatedMember.hasUserPrivilege(DaoMember.Role.ADMIN)) {
            session.notifyError(getRefusalReason());
            return new LoginPageUrl();
        }
        try {
            for (final Identifiable<?> content : contents) {

                switch (action) {
                    case DELETE:
                        ((UserContent<?>) content).delete();
                        break;
                    case RESTORE:
                        ((UserContent<?>) content).restore();
                        break;
                    case LOCK:
                        ((Kudosable<?>) content).lockPopularity();
                        break;
                    case UNLOCK:
                        ((Kudosable<?>) content).unlockPopularity();
                        break;
                    case SETSTATE:
                        if (stateToSet != null && stateToSet != DisplayableState.NO_FILTER) {
                            ((Kudosable<?>) content).setState(DisplayableState.getState(stateToSet));
                        }
                        break;
                    case SET_VALIDATION_DATE:
                        session.notifyBad("SetValidationDate not implemented yet");
                        break;
                    case UPDATE_DEVELOPMENT_STATE:
                        ((Feature) content).updateDevelopmentState();
                        break;
                    case COMPUTE_SELECTED_OFFER:
                        ((Feature) content).computeSelectedOffer();
                        break;
                    case SET_FEATURE_IN_DEVELOPMENT:
                        Feature feature = (Feature) content;
                        if (feature.getSelectedOffer() == null || feature.getSelectedOffer().getAmount().compareTo(feature.getContribution()) > 0) {
                            session.notifyBad("There is no offer or not enough money. So no developement state for id: " + feature.getId());
                        } else {
                            feature.setFeatureState(FeatureState.DEVELOPPING);
                        }
                        break;
                    case VALIDATE_BATCH:
                        ((Batch) content).validate();
                        break;
                    case FORCE_VALIDATE_BATCH:
                        ((Batch) content).forceValidate();
                        break;
                    default:
                        break;
                }
            }
        } catch (UnauthorizedOperationException e) {
            session.notifyError(getRefusalReason());
            return new LoginPageUrl();
        }
        return session.getLastStablePage();
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyError("Ça marche pas");
        session.notifyList(url.getMessages());
        return session.getLastStablePage();
    }

    @Override
    protected String getRefusalReason() {
        return tr("You have to be admin to do this action.");
    }

    @Override
    protected void transmitParameters() {
        // TODO Auto-generated method stub
    }

}
