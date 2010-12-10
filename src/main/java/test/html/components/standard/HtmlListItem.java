/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package test.html.components.standard;

import test.html.HtmlNode;
import test.html.HtmlText;
import test.pages.HtmlContainerElement;

/**
 * 
 * @author fred
 */
public class HtmlListItem extends HtmlContainerElement {

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
