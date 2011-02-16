package com.bloatit.web.actions;

import java.util.List;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.UserContent;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("doadministration")
public class AdministrationAction extends Action {

    @RequestParam(name = "id", role = Role.POST)
    private final List<UserContent> contents;

    @RequestParam(name = "action", role = Role.POST)
    private final String action;

    private final AdministrationActionUrl url;

    public AdministrationAction(AdministrationActionUrl url) {
        super(url);
        this.url = url;
        contents = url.getContents();
        action = url.getAction();
        System.err.println(action);
        System.err.println(contents);
    }

    @Override
    protected Url doProcess() throws RedirectException {
        for (UserContent<?> content : contents) {
            System.out.println(content);
            if (action.equals("delete")) {
                content.delete();
            } else if (action.equals("restore")) {
                content.restore();
            }
        }
        return new AdministrationPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyError("Ça marche pas");
        session.notifyList(url.getMessages());
        return new AdministrationPageUrl();
    }

}
