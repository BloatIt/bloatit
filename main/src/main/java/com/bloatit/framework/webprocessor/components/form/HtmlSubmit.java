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

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.HtmlSimpleInput.InputType;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;

public final class HtmlSubmit extends HtmlLeaf {

    /**
     * Creates a HtmlButton
     *
     * @param value the text that will be shown on the button
     */
    public HtmlSubmit(final String value) {
        super();
        final HtmlElement button = new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.SUBMIT_INPUT));
        add(new HtmlDiv().add(button).setCssClass("field"));
        button.addAttribute("value", value);
    }
}
