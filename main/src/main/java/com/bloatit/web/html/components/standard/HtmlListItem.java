/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.bloatit.web.html.components.standard;

import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlNode;

/**
 * @author fred
 */
public class HtmlListItem extends HtmlBranch {

    public HtmlListItem(final HtmlNode node) {
        super("li");
        add(node);
    }
}
