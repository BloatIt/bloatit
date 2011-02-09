package com.bloatit.web.actions;

import java.util.List;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Demand;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends Action {

    @RequestParam(name = "ids", role = Role.POST)
    private final List<Demand> demands;

    public AdministrationAction(AdministrationActionUrl url) {
        super(url);
        demands = url.getDemands();
    }

    @Override
    protected Url doProcess() throws RedirectException {
        for (Demand demand : demands) {
            session.notifyGood(demand.toString());
        }
        return new AdministrationPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyError("Ça marche pas");
        return new AdministrationPageUrl();
    }

}
