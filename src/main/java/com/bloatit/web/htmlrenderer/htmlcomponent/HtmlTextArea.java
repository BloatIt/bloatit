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

public class HtmlTextArea extends HtmlComponent {

    private String defaultValue = null;
    private final String label;

    public HtmlTextArea(String label){
        this.label = label;
    }

    public HtmlTextArea(){
        this("");
    }

    @Override
    public void generate(HtmlResult htmlResult) {
        htmlResult.write("<p>"+ this.generateLabel()+"</p>");
        htmlResult.write("<p>");
        htmlResult.indent();
        if (defaultValue != null) {
            htmlResult.write("<textarea name=\"" + name + "\" id=\""+this.name+"\">"+defaultValue+"</textarea>");
        }else{
            htmlResult.write("<textarea name=\"" + name + "\" id=\""+this.name+"\"></textarea>");
        }
        htmlResult.unindent();
        htmlResult.write("</p>");
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    private String generateLabel(){
        if(this.label.equals("")){
            return "";
        }
        return "<label for=\""+this.name+"\">"+this.label+"</label>";
    }
}
