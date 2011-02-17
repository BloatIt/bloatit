package com.bloatit.web.actions;

import java.util.List;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.admin.DemandAdmin;
import com.bloatit.model.admin.KudosableAdmin;
import com.bloatit.model.admin.UserContentAdmin;
import com.bloatit.web.pages.admin.AdminActionManager;
import com.bloatit.web.pages.admin.DisplayableDemandState;
import com.bloatit.web.pages.admin.DisplayableState;
import com.bloatit.web.url.AdministrationActionUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends Action {

    public static final String POPULARITY_STATE_CODE = "popularitystate";
    public static final String DEMAND_STATE_CODE = "demandstate";

    @RequestParam(name = "id", role = Role.POST)
    private final List<Integer> contents;

    @RequestParam(name = "action", role = Role.POST)
    private final AdminActionManager.Action action;

    @RequestParam(name = POPULARITY_STATE_CODE, role = Role.POST)
    @ParamConstraint(optional = true)
    private final DisplayableState stateToSet;

    @RequestParam(name = DEMAND_STATE_CODE, role = Role.POST)
    @ParamConstraint(optional = true)
    private DisplayableDemandState demandState;
    
    private final AdministrationActionUrl url;


    public AdministrationAction(AdministrationActionUrl url) {
        super(url);
        this.url = url;
        contents = url.getContents();
        action = url.getAction();
        stateToSet = url.getStateToSet();
        demandState = url.getDemandState();
    }

    @Override
    protected Url doProcess() throws RedirectException {
        for (Integer content : contents) {

            switch (action) {
            case DELETE:
                UserContentAdmin.createUserContent(content).delete();
                break;
            case RESTORE:
                UserContentAdmin.createUserContent(content).restore();
                break;
            case LOCK:
                KudosableAdmin.createKudosable(content).lockPopularity();
                break;
            case UNLOCK:
                KudosableAdmin.createKudosable(content).unlockPopularity();
                break;
            case SETSTATE:
                if (stateToSet != null && stateToSet != DisplayableState.NO_FILTER) {
                    KudosableAdmin.createKudosable(content).setState(DisplayableState.getState(stateToSet));
                }
                break;
            case COMPUTE_SELECTED_OFFER:
                DemandAdmin.createDemand(content).computeSelectedOffer();
                break;
            case SET_DEMAND_STATE:
                if (demandState != null && demandState != DisplayableDemandState.NO_FILTER) {
                    DemandAdmin.createDemand(content).setDemandState(DisplayableDemandState.getDemandState(demandState));
                }
                break;
            default:
                break;
            }
        }
        return session.getLastStablePage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyError("Ça marche pas");
        session.notifyList(url.getMessages());
        return session.getLastStablePage();
    }

}
