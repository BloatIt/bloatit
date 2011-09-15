/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.bloatit.framework.webprocessor.components;

import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;

public class HtmlListItem extends HtmlBranch {

    public HtmlListItem(final HtmlNode node) {
        super("li");
        add(node);
    }
}
