package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;

public class SidebarMarkdownHelp extends SideBarDocumentationBlock {

    public SidebarMarkdownHelp() {
        super("markdown", Context.tr("+ Show/Hide markdown help"));
    }

}
