package com.bloatit.web.linkable.admin.notify;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.AdminGlobalNotificationActionUrl;
import com.bloatit.web.url.AdminGlobalNotificationPageUrl;

@ParamContainer("admin/donotify")
public class AdminGlobalNotificationAction extends AdminAction {
    private final AdminGlobalNotificationActionUrl url;

    @RequestParam(role = Role.POST)
    @Optional
    private final String message;

    public AdminGlobalNotificationAction(final AdminGlobalNotificationActionUrl url) {
        super(url);
        this.url = url;
        this.message = url.getMessage();
    }

    @Override
    protected Url doProcessAdmin() throws UnauthorizedOperationException {
        if (message != null) {
            session.notifyGood("The new notification message will be : " + message + ".");
        } else {
            session.notifyGood("Global notification message removed.");
        }
        Context.setGlobalNotification(message);
        return new AdminGlobalNotificationPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new AdminGlobalNotificationPageUrl();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getMessageParameter());
    }
}
