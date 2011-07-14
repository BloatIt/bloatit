package com.bloatit.web.linkable.admin.news;

import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminAction;
import com.bloatit.web.url.AdminNewsActionUrl;
import com.bloatit.web.url.AdminNewsPageUrl;

@ParamContainer("admin/donews")
public class AdminNewsAction extends AdminAction {

    @RequestParam(role = Role.POST)
    @MinConstraint(min = 10, message = @tr("News feed message must be more than %constraint% characters."))
    @MaxConstraint(max = 140, message = @tr("News feed message must be less than %constraint% characters."))
    private final String message;

    private final AdminNewsActionUrl url;

    public AdminNewsAction(final AdminNewsActionUrl url) {
        super(url);
        this.url = url;
        this.message = url.getMessage();
    }

    @Override
    protected Url doProcessAdmin() throws UnauthorizedOperationException {
        new NewsFeed(message);
        session.notifyGood(Context.tr("Message created !"));
        return new AdminNewsPageUrl();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new AdminNewsPageUrl();
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getMessageParameter());
    }
}
