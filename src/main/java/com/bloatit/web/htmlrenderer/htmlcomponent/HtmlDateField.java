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
import java.util.Date;

public class HtmlDateField extends HtmlComponent{
    // TODO

    private final HtmlTextField textField = new HtmlTextField();
    private Date defaultValue;
    private String label;

    public HtmlDateField(){
        this(null);
    }

    public HtmlDateField(Date defaultValue) {
        this(defaultValue, "");
    }

    public HtmlDateField(Date defaultValue, String label) {
        this.defaultValue = defaultValue;
        this.label = label;
    }

    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public void generate(HtmlResult htmlResult) {
        if(this.defaultValue != null ){
            this.textField.setDefaultValue(defaultValue.toString());
        }

        if(this.label != null){
            this.textField.setLabel(this.label);
        }

        this.textField.generate(htmlResult);
    }
}