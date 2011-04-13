package com.bloatit.framework.webprocessor.components.renderer;

import com.bloatit.framework.webprocessor.components.meta.XmlText;

/**
 * <p>
 * The mother classes of all renderers that can be used to have special text
 * rendering
 * </p>
 */
public abstract class HtmlTextRenderer extends XmlText {

    /**
     * Creates an htmlRenderer
     * 
     * @param text
     */
    public HtmlTextRenderer(final String text) {
        super();
        super.content = doRender(text);
    }

    /**
     * This method does the rendering
     * 
     * @param text the string to render
     * @return the rendered string
     */
    protected abstract String doRender(String text);
}
