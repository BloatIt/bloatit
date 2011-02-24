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

package com.bloatit.framework.webserver.components.form;

import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.form.HtmlSimpleInput.InputType;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlLeaf;

/**
 * <p>
 * Class used to create Html buttons
 * </p>
 * <p>
 * Html buttons are <b>NOT</b> to be mistaken with html submits. If you want to
 * submit a form, use {@link HtmlSubmit}
 * </p>
 *
 * @see HtmlSubmit
 */
public final class HtmlButton extends HtmlLeaf {

    private final HtmlElement button = new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.BUTTON_INPUT));

    /**
     * <p>
     * Creates a HtmlButton
     * </p>
     *
     * @param label the text that will be shown on the button
     */
    public HtmlButton(final String value) {
        super();
        add(new HtmlParagraph().add(this.button));
        this.button.addAttribute("value", value);
    }
}
