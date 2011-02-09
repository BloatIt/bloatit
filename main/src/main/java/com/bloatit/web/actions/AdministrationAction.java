package com.bloatit.web.actions;

import java.util.List;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends Action {

    @RequestParam(name = "coucou", role = Role.POST)
    private final List<String> test;


    public AdministrationAction(AdministrationActionUrl url) {
        super(url);
        test = url.getTest();
    }

    @Override
    protected Url doProcess() throws RedirectException {
        for (String str : test) {
            session.notifyGood(str);
        }
        return new AdministrationPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyError("Ça marche pas");
        return new AdministrationPageUrl();
    }

}
