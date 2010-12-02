/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.htmlrenderer.htmlcomponent;

import com.bloatit.web.htmlrenderer.HtmlResult;

public class HtmlTextField extends HtmlComponent {

    private String defaultValue = null;
    private String label;

    /**
     * Creates a textField with a default value and the label
     * @param defaultValue
     *      The value displayed by default in the textField after page
     *      loaded
     * @param label
     *      The text displayed next to the textField
     */
    public HtmlTextField(String defaultValue, String label){
        this.label = label;
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a textField with a default value
     * @param defaultValue
     *      The value displayed by default in the textField after page
     *      loaded
     */
    public HtmlTextField(String defaultValue){
        this(defaultValue, "");
    }

    /**
     * Creates a textField with no default value and no associated label
     */
    public HtmlTextField(){
        this("");
    }

    /**
     * Sets the display that will be used along with the text field
     * @param label
     *      The string that will ne displayed next to the text field
     */
    public void setLabel(String label){
        this.label = label;
    }

    /**
     * Sets the value displayed in the text field after page is loaded
     * @param defaultValue
     *      The displayed value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        if (defaultValue == null) {
            htmlResult.write("<p>"+ this.generateLabel() + "<input name=\"" + name + "\" type=\"text\" id=\""+this.name+"\"/></p>");
        } else {
            htmlResult.write("<p>"+ this.generateLabel() + "<input name=\"" + name + "\" type=\"text\" id=\""+this.name+"\" value=\"" + defaultValue + "\" /></p>");
        }
    }

    private String generateLabel(){
        if(this.label.equals("")){
            return "";
        }
        return "<label for=\""+this.name+"\">"+this.label+"</label>";
    }
}
