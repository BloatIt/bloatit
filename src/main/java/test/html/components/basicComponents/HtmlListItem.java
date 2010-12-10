/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package test.html.components.basicComponents;

import test.html.HtmlElement;
import test.html.HtmlNode;
import test.html.HtmlText;

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

    public HtmlListItem(String content) {
        this(new HtmlText(content));
    }
}
