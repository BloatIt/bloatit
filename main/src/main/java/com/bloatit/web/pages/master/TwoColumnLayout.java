package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class TwoColumnLayout extends HtmlDiv {

    private final HtmlDiv leftColumn;
    private final HtmlDiv rightColumn;

    public TwoColumnLayout() {
        this(false);
    }

    public TwoColumnLayout(boolean box) {
        super("two_column");
        HtmlDiv leftColumnBlock = new HtmlDiv((box ?"left_column_block":"left_column_block_without_box"));
        HtmlDiv rightColumnBlock = new HtmlDiv("right_column_block");
        leftColumn = new HtmlDiv((box ?"left_column":"left_column_without_box"));
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
