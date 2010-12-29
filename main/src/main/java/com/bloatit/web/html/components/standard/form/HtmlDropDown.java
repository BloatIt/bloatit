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

package com.bloatit.web.html.components.standard.form;

import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Class to handle Html drop down boxes (aka Html select).</p>
 * <p>Usage is : create the object, then use addText to add a new line</p>
 */
public class HtmlDropDown extends HtmlFormField<Integer> {
    private List<HtmlBranch> components = new ArrayList<HtmlBranch>();

    public HtmlDropDown(final String name) {
        super(new HtmlGenericElement("select"), name);
    }

    public HtmlDropDown(final String name, final String label) {
        super(new HtmlGenericElement("select"), name, label);
    }

    /**
     * <p>Sets the default value of the drop down box</p>
     * <p>Do not use this method twice</p>
     * @param value the index of the element, 0 being the first element
     * inserted with addText
     */
    @Override
    protected void doSetDefaultValue(final Integer value) {
        components.get(value).addAttribute("selected", "selected");
    }

    public HtmlElement add(String text) {
        return this.add(text, text);
    }

    public HtmlElement add(String text, String value) {
        final HtmlGenericElement opt = new HtmlGenericElement("option");
        opt.addText(text);
        opt.addAttribute("value", value);
        ((HtmlBranch)element).add(opt);
        return this;
    }
}