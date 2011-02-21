/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.bloatit.framework.webserver.components;

import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.XmlNode;

public class HtmlListItem extends HtmlBranch {

    public HtmlListItem(final XmlNode node) {
        super("li");
        add(node);
    }
}
