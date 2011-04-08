package com.bloatit.framework.webprocessor.components.advanced;

import com.bloatit.framework.webprocessor.components.HtmlGenericElement;

/**
 * TODO : Fred has to comment this
 */
public class HtmlClearer extends HtmlGenericElement {

    public HtmlClearer() {
        super("hr");
        setCssClass("clearer");
    }

    @Override
    public boolean selfClosable() {
        return true;
    }

}
