/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.bloatit.web.html.components.standard;

import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlBranch;


/**
 * 
 * @author fred
 */
public class HtmlListItem extends HtmlBranch {

    public HtmlListItem(final HtmlNode node) {
        super("li");
        add(node);
    }
}
