package com.bloatit.framework.webserver.masters;

import java.io.IOException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.components.meta.HtmlElement;

public abstract class Page extends HtmlElement implements Linkable {

    @Override
    public final void writeToHttp(final HttpResponse response) throws RedirectException, IOException {
        create();
        response.writePage(this);
    }

    protected abstract void create() throws RedirectException;

    protected abstract String getTitle();

    public abstract boolean isStable();

    @Override
    public final boolean selfClosable() {
        return false;
    }
}
