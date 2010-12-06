/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package test.htmlComponents;

import test.HtmlElement;
import test.HtmlText;

public class HtmlTextArea extends HtmlElement implements HtmlNamedNode {

    private HtmlElement textarea = null;
    private HtmlElement label = null;

    public HtmlTextArea(String label) {
        super("p");
        this.textarea = new HtmlElement("textarea");
        this.label = new HtmlElement("label");

        this.add(this.label.add(new HtmlText(label)));
        this.add(textarea);
    }

    public HtmlTextArea() {
        super("p");
        this.textarea = new HtmlElement("textarea");
        this.add(textarea);
    }

    public void setName(String name) {
        textarea.addAttribute("name", name);
        textarea.addAttribute("id", name);
        if (label != null) {
            label.addAttribute("for", name);
        }
    }

    public void setDefaultValue(String defaultValue) {
        textarea.addAttribute("value", defaultValue);
    }

}
