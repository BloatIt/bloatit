/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package test.htmlComponents;

import test.HtmlElement;
import test.HtmlNode;

/**
 * 
 * @author fred
 */
public class HtmlListItem extends HtmlElement {

    public HtmlListItem(HtmlNode node) {
        super("li");
        add(node);
    }

    public HtmlListItem(String cssClass, HtmlNode node) {
        this(node);
        addAttribute("class", cssClass);
    }

}
