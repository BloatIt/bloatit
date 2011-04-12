package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.ModelAccessor;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;

public abstract class Page extends HtmlElement implements Linkable {

    public Page() {
        super();
        ModelAccessor.setReadOnly();
    }

    @Override
    public final void writeToHttp(final HttpResponse response, final WebProcessor server) throws RedirectException, IOException {
        create();
        response.writePage(this);
    }

    @Override
    public final boolean selfClosable() {
        return false;
    }

    // -----------------------------------------------------------------------
    // Template method pattern: Abstract methods
    // -----------------------------------------------------------------------

    public abstract boolean isStable();

    protected abstract void create() throws RedirectException;

    protected abstract String getTitle();
}
