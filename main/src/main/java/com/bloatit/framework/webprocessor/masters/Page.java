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
    public final void writeToHttp(final HttpResponse response, WebProcessor server) throws RedirectException, IOException {
        create();
        generateDependencies();
        response.writePage(this);
    }

    @Override
    public final boolean selfClosable() {
        return false;
    }

    protected abstract void create() throws RedirectException;

    public abstract boolean isStable();

    protected abstract String getTitle();

    /**
     * Generate dependancies to javascript and css files.
     * <p>
     * This method needs to be called before writing the page
     * </p>
     */
    protected abstract void generateDependencies();
}
