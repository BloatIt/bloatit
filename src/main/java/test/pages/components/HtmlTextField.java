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

package test.pages.components;

import test.HtmlElement;
import test.HtmlText;

public class HtmlTextField extends test.HtmlElement {

    private HtmlInput input = null;
    private HtmlElement label = null;

    /**
     * Creates a textField with a default value and the label
     * 
     * @param defaultValue The value displayed by default in the textField after page
     * loaded
     * @param label The text displayed next to the textField
     */
    public HtmlTextField(String defaultValue, String label) {
        super("p");
        this.input = new HtmlInput("text");
        this.label = new HtmlElement("label");

        this.add(this.label.add(new HtmlText(label)));
        this.add((input).addAttribute("value", defaultValue));
    }

    /**
     * Creates a textField with a default value
     * 
     * @param defaultValue The value displayed by default in the textField after page
     * loaded
     */
    public HtmlTextField(String defaultValue) {
        super("p");
        this.input = new HtmlInput("text");
        this.add(input.addAttribute("value", defaultValue));
    }

    /**
     * Creates a textField with no default value and no associated label
     */
    public HtmlTextField() {
        super("p");
        this.input = new HtmlInput("text");
        this.add(input);
    }

    public void setName(String name) {
        input.setName(name);
        if (label != null) {
            label.addAttribute("for", name);
        }
    }
}
