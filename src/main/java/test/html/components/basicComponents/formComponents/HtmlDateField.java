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

package test.html.components.basicComponents.formComponents;

import com.bloatit.web.utils.BloatitDate;

/**
 * Class used to create input fields used to input date
 */
public class HtmlDateField extends HtmlFormField<BloatitDate> {

    public HtmlDateField(String name) {
        super(new HtmlSimpleInput("text"), name);
    }

    public HtmlDateField(String name, String label) {
        super(new HtmlSimpleInput("text"), name, label);
    }

    @Override
    protected void doSetDefaultValue(BloatitDate value) {
        this.addAttribute("value", value.toString(BloatitDate.FormatStyle.SHORT));
    }
}
