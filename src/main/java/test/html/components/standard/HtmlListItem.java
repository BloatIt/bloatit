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

    public HtmlListItem(final HtmlNode node) {
        super("li");
        add(node);
    }

    public HtmlListItem(final String cssClass, final HtmlNode node) {
        this(node);
        addAttribute("class", cssClass);
    }

    public HtmlListItem(final String content) {
        this(new HtmlText(content));
    }
}
