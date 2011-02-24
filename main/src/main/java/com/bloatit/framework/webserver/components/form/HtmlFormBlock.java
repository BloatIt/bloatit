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

import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;

/**
 * <p>
 * Creates display blocks in a form
 * </p>
 * <p>
 * Form blocks should be used to separate various sections of a form. For
 * example personnal user data could be in one formblock and website preferences
 * in another one.
 * </p>
 *
 * <pre>
 * {@code<fieldset>
 *      <legend>my legend here</legend>
 *      <form element />
 *      <another form element />
 * </fieldset>}
 * </pre>
 */
public final class HtmlFormBlock extends HtmlBranch {

    /**
     * <p>
     * Create a new displayed form block, with a displayed legend
     * </p>
     *
     * @param legend The text displayed to explain content of the form block
     */
    public HtmlFormBlock(final String legend) {
        super("fieldset");
        final HtmlBranch legendBranch = new HtmlGenericElement("legend");
        legendBranch.addText(legend);
        add(legendBranch);
    }
}
