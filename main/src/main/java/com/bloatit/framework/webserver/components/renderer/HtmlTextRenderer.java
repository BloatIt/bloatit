package com.bloatit.framework.webserver.components.renderer;

import com.bloatit.framework.webserver.components.meta.HtmlTagText;

public abstract class HtmlTextRenderer extends HtmlTagText {

    public HtmlTextRenderer(final String text) {
        super();
        super.content = doRender(text);
    }

    protected abstract String doRender(String text);
}
