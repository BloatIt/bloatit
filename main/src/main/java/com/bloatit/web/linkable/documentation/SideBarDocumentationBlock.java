package com.bloatit.web.linkable.documentation;

import com.bloatit.web.linkable.documentation.HtmlDocumentationRenderer.DocumentationType;
import com.bloatit.web.pages.master.SideBarElementLayout;

public class SideBarDocumentationBlock extends SideBarElementLayout {
    public SideBarDocumentationBlock(String key) {
        add(new HtmlDocumentationRenderer(DocumentationType.FRAME, key));
    }
}
