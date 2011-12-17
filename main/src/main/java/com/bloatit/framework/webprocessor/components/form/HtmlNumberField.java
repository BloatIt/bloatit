//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.components.form;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;

/**
 * <p>
 * A field used to ask the user to input an amount of money
 * </p>
 */
public class HtmlNumberField extends HtmlFormField {

    /**
     * <p>
     * Creates a money field with a given <code>name</code>
     * </p>
     *
     * @param name the value of the html attribute <code>name</code>
     */
    public HtmlNumberField(final String name) {
        super(new InputField(), name);
    }

    public static class InputField extends InputBlock {

        private final HtmlSimpleInput input;
        private final HtmlDiv content;

        public InputField() {
            content = new HtmlDiv("money_input");
            input = new HtmlSimpleInput("number");
            input.addAttribute("autocomplete", "off");
            input.addAttribute("pattern", "[0-9., \\-]*");
            content.add(input);
        }

        @Override
        public HtmlElement getInputElement() {
            return input;
        }

        @Override
        public HtmlElement getContentElement() {
            return content;
        }

    }

    /**
     * <p>
     * Creates a money field with a given <code>name</code> and some text used
     * to explain the usage of the field
     * </p>
     *
     * @param name the value of the html attribute <code>name</code>
     * @param label some text displayed to explain how to use the field
     */
    public HtmlNumberField(final String name, final String label) {
        super(new InputField(), name, label);
    }

    @Override
    protected void doSetDefaultStringValue(final String defaultValueAsString) {
        addAttribute("value", defaultValueAsString);
    }
}
