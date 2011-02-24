package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class TwoColumnLayout extends HtmlDiv {

    private final HtmlDiv leftColumn;
    private final HtmlDiv rightColumn;

    public TwoColumnLayout() {
        super("two_column");
        HtmlDiv leftColumnBlock = new HtmlDiv("left_column_block");
        HtmlDiv rightColumnBlock = new HtmlDiv("right_column_block");
        leftColumn = new HtmlDiv("left_column");
        rightColumn = new HtmlDiv("right_column");
        leftColumnBlock.add(leftColumn);
        rightColumnBlock.add(rightColumn);
        add(leftColumnBlock);
        add(rightColumnBlock);
        add(new HtmlClearer());
    }

    public void addLeft(XmlNode element) {
        leftColumn.add(element);
    }

    public void addRight(XmlNode element) {
        rightColumn.add(element);
    }

}
