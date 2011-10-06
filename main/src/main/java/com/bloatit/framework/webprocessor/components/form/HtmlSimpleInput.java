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

import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;

/**
 * Meta class used to create various input fields
 */
public class HtmlSimpleInput extends HtmlLeaf {

    public enum InputType {
        TEXT_INPUT, PASSWORD_INPUT, FILE_INPUT, CHECKBOX_INPUT, RADIO_INPUT, BUTTON_INPUT, SUBMIT_INPUT, RESET_INPUT, HIDDEN_INPUT
    }

    private static final String TEXT = "text";
    private static final String PASSWORD = "password";
    private static final String FILE = "file";
    private static final String CHECKBOX = "checkbox";
    private static final String RADIO = "radio";
    private static final String BUTTON = "button";
    private static final String SUBMIT = "submit";
    private static final String RESET = "reset";
    private static final String HIDDEN = "hidden";

    public HtmlSimpleInput(final String type) {
        super("input");
        addAttribute("type", type);
    }

    protected final HtmlSimpleInput setName(final String name) {
        addAttribute("name", name).addAttribute("id", name);
        return this;
    }

    public static String getInput(final InputType type) {
        switch (type) {
            case TEXT_INPUT:
                return TEXT;
            case BUTTON_INPUT:
                return BUTTON;
            case CHECKBOX_INPUT:
                return CHECKBOX;
            case FILE_INPUT:
                return FILE;
            case HIDDEN_INPUT:
                return HIDDEN;
            case PASSWORD_INPUT:
                return PASSWORD;
            case RADIO_INPUT:
                return RADIO;
            case RESET_INPUT:
                return RESET;
            case SUBMIT_INPUT:
                return SUBMIT;
            default:
                assert false;
                return TEXT;
        }
    }

}
