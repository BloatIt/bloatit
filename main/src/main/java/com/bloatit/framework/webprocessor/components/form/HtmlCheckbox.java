/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.webprocessor.components.form;

import com.bloatit.framework.webprocessor.components.form.HtmlSimpleInput.InputType;

/**
 * <p>
 * A class that represents an html {@code <input type="checkbox" />} tag (a
 * simple multiple choice answer tickable box)
 * </p>
 * <p>
 * <b>Note</b>: One shouldn't create much checkboxes directly, but should
 * instead use the beautiful {@link CheckBoxGroup} class
 * </p>
 * 
 * @see CheckBoxGroup
 * @see CheckBoxGroup#addCheckBox(String, String)
 */
public final class HtmlCheckbox extends HtmlFormField<Boolean> {

    /**
     * <p>
     * Creates a new checkbox with a given attribute <code>name</code> and a
     * given relative position of the label compared to the checkbox
     * </p>
     * <p>
     * Example of <code>LabelPosition.BEFORE</code> :<br />
     * {@code <label form="anId3">I like nothing</label><input type="checkbox" name="nothin" id="anId3" />}
     * </p>
     * <p>
     * Example of <code>LabelPosition.AFTER</code> :<br />
     * {@code <input type="checkbox" name="nothin" id="anId3" /><label form="anId3">I like nothing</label>}
     * </p>
     * <p>
     * <b>NOTE</b>: This method shouldn't be used, a checkbox without label is
     * useless
     * </p>
     * 
     * @param name the value of the attribute <code>name</code> of the checkbox
     * @param labelPosition the relative position of the label compared to the
     *            checkbox
     */
    public HtmlCheckbox(final String name, final LabelPosition labelPosition) {
        super(InputBlock.create(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.CHECKBOX_INPUT))), name, labelPosition);
    }

    /**
     * <p>
     * Creates a new checkbox with a given attribute <code>name</code>, a
     * <code>label</code> and a given relative position of the label compared to
     * the checkbox
     * </p>
     * <p>
     * Example of <code>LabelPosition.BEFORE</code> :<br />
     * {@code <label form="anId3">I like nothing</label><input type="checkbox" name="nothin" id="anId3" />}
     * </p>
     * <p>
     * Example of <code>LabelPosition.AFTER</code> :<br />
     * {@code <input type="checkbox" name="nothin" id="anId3" /><label form="anId3">I like nothing</label>}
     * </p>
     * 
     * @param name the value of the attribute <code>name</code> of the checkbox
     * @param label the text displayed to explain the use of this checkbox
     * @param position the relative position of the label compared to the
     *            checkbox
     */
    public HtmlCheckbox(final String name, final String label, final LabelPosition position) {
        super(InputBlock.create(new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.CHECKBOX_INPUT))), name, label, position);
    }

    @Override
    protected void doSetDefaultValue(final Boolean value) {
        if (value.booleanValue()) {
            addAttribute("checked", "checked");
        }
    }

    @Override
    protected void doSetDefaultStringValue(final String defaultValueAsString) {
        if (Boolean.parseBoolean(defaultValueAsString) || defaultValueAsString.equals("on")) {
            addAttribute("checked", "checked");
        }
    }
    
    public void setDefaultBooleanValue(boolean value){
        setDefaultValue(Boolean.toString(value));
    }
}
