/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.web.htmlrenderer.HtmlResult;

/**
 * 
 * @author fred
 */
public class HtmlListItem extends HtmlComponent {

    final private String text;
    final private String cssClass;

    public HtmlListItem(String text) {
        this.text = text;
        this.cssClass = null;
    }

    public HtmlListItem(String cssClass, String text) {
        this.text = text;
        this.cssClass = cssClass;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        if (cssClass == null) {
            htmlResult.write("<li>" + text + "</li>");
        } else {
            htmlResult.write("<li class=\"" + cssClass + "\">" + text + "</li>");
        }
    }

}
