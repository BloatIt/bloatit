package com.bloatit.web.linkable.messages;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.MessageConsultPageUrl;

@ParamContainer("messages/consult")
public class MessageConsultPage extends LoggedPage {
    public MessageConsultPage(final MessageConsultPageUrl url) {
        super(url);
    }

    @Override
    public void processErrors() throws RedirectException {
        // TODO maybe we should process the errors here..
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {
        return null;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to check your messages.");
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Bloatit private messages.");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        throw new NotImplementedException();
    }



}
