package com.bloatit.web.linkable.admin.exception;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.ExceptionAdministrationActionUrl;
import com.bloatit.web.url.ExceptionAdministrationPageUrl;

/**
 * Simple admin page that can be used to throw some high level bloatit
 * exceptions
 */
@ParamContainer("admin/exception")
public class ExceptionAdministrationPage extends AdminPage {

    public enum ErrorType {
        BAD_PROGRAMMER, EXTERNAL_ERROR, MEAN_USER, SHALL_NOT_PASS
    }

    public ExceptionAdministrationPage(final ExceptionAdministrationPageUrl url) {
        super(url);
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final HtmlDiv master = new HtmlDiv("padding_box");
        final HtmlList list = new HtmlList();
        list.add(new ExceptionAdministrationActionUrl(getSession().getShortKey(), ErrorType.BAD_PROGRAMMER).getHtmlLink("Bad programmer exception"));
        list.add(new ExceptionAdministrationActionUrl(getSession().getShortKey(), ErrorType.EXTERNAL_ERROR).getHtmlLink("External error exception"));
        list.add(new ExceptionAdministrationActionUrl(getSession().getShortKey(), ErrorType.MEAN_USER).getHtmlLink("Mean user exception"));
        list.add(new ExceptionAdministrationActionUrl(getSession().getShortKey(), ErrorType.SHALL_NOT_PASS).getHtmlLink("Shall not pass exception"));
        master.add(list);
        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb crumb = new Breadcrumb();
        crumb.pushLink(new AdminHomePageUrl().getHtmlLink("admin"));
        crumb.pushLink(new ExceptionAdministrationPageUrl().getHtmlLink("exceptions"));
        return crumb;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("administration - throw exception");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
