/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package com.bloatit.framework.webprocessor.components;

import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

public class HtmlListItem extends HtmlBranch {

    public HtmlListItem(final XmlNode node) {
        super("li");
        add(node);
    }
}
