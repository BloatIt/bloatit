package com.bloatit.framework.webserver.masters;

import java.io.IOException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.ModelAccessor;
import com.bloatit.framework.webserver.components.meta.HtmlElement;

public abstract class Page extends HtmlElement implements Linkable {

    public Page() {
        super();
        ModelAccessor.setReadOnly();
    }

    @Override
    public final void writeToHttp(final HttpResponse response) throws RedirectException, IOException {
        create();
        response.writePage(this);
    }

    @Override
    public final boolean selfClosable() {
        return false;
    }

    protected abstract void create() throws RedirectException;

    public abstract boolean isStable();

    protected abstract String getTitle();
}
