package com.bloatit.web.pages.master.sidebar;

import java.util.ArrayList;

import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;

public class PluginBox extends PlaceHolderElement {
    private final ArrayList<HtmlElement> plugins = new ArrayList<HtmlElement>();
    private final PlaceHolderElement pageContent;

    public PluginBox(final Url url) {
        pageContent = new PlaceHolderElement();
        plugins.add(pageContent);
        plugins.add(new SideBarBugReportBlock(url));
        for (final HtmlElement plugin : plugins) {
            add(plugin);
        }
    }

    public void addPageContent(final SideBarElementLayout content) {
        pageContent.add(content);
    }
}
