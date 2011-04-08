package com.bloatit.web.pages.master.sidebar;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.Url;

public class TwoColumnLayout extends HtmlDiv {

    private final HtmlDiv leftColumn;
    private final HtmlDiv rightColumn;
    private final PluginBox plugins;

    public TwoColumnLayout(Url url) {
        this(false, url);
    }

    /**
     * Creates a two column layout with, or without a box around it
     * 
     * @param box <i>true</i> if a box should surround the whole layout,
     *            <i>false</i> otherwise
     */
    public TwoColumnLayout(boolean box, Url url) {
        super("two_column");
        HtmlDiv leftColumnBlock = new HtmlDiv((box ? "left_column_block" : "left_column_block_without_box"));
        HtmlDiv rightColumnBlock = new HtmlDiv("right_column_block");
        leftColumn = new HtmlDiv((box ? "left_column" : "left_column_without_box"));
        rightColumn = new HtmlDiv("right_column");
        
        plugins = new PluginBox(url);
        rightColumn.add(plugins);
        
        leftColumnBlock.add(leftColumn);
        rightColumnBlock.add(rightColumn);
        add(leftColumnBlock);
        add(rightColumnBlock);
        add(new HtmlClearer());
    }

    /**
     * Adds an element to the core of the page
     * 
     * @param element the element to add
     */
    public void addLeft(HtmlElement element) {
        leftColumn.add(element);
    }
 
    /**
     * Adds an element to the right bar of the page
     * 
     * @param element
     */
    public void addRight(SideBarElementLayout element) {
//        rightColumn.add(element);
        plugins.addPageContent(element);
    }
}
