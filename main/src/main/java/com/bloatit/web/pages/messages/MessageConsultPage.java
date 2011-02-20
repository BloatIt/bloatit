package com.bloatit.web.pages.messages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.MessageConsultPageUrl;

@ParamContainer("messages/consult")
public class MessageConsultPage extends LoggedPage {
    public MessageConsultPage(final MessageConsultPageUrl url) {
        super(url);
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

}
