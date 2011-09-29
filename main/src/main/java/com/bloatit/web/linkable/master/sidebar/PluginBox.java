/*
 * 
 */
package com.bloatit.web.linkable.master.sidebar;

import java.util.ArrayList;

import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.linkable.meta.feedback.SideBarFeedbackBlock;

public class PluginBox extends PlaceHolderElement {
    private final ArrayList<HtmlElement> plugins = new ArrayList<HtmlElement>();
    private final PlaceHolderElement pageContent;

    protected PluginBox(final Url url) {
        pageContent = new PlaceHolderElement();
        plugins.add(pageContent);
        plugins.add(new SideBarFeedbackBlock(url));
        for (final HtmlElement plugin : plugins) {
            add(plugin);
        }
    }

    protected void addPageContent(final SideBarElementLayout content) {
        pageContent.add(content);
    }
}
