/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www enum LabelPosition{
 * BEFORE, AFTER }.gnu.org/licenses/>.
 */
package com.bloatit.framework.webserver.components.form;

import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.meta.HtmlLeaf;

/**
 * <p>
 * A CheckBoxGroup represents a set of checkboxes answering a similar question
 * </p>
 * <p>
 * Example :
 *
 * <pre>
 * HtmlForm form = new HtmlForm(&quot;anAction&quot;);
 * form.addText(&quot;What do you like ?&quot;);
 * CheckBoxGroup halfOpenQuestion = new CheckBoxGroup(LabelPosition.AFTER);
 * halfOpenQuestion.addCheckBox(&quot;chocolate&quot;, &quot;I like chocolate&quot;);
 * halfOpenQuestion.addCheckBox(&quot;coke&quot;, &quot;I like coke&quot;);
 * halfOpenQuestion.addCheckBox(&quot;nothing&quot;, &quot;I like nothing&quot;);
 * form.add(halfOpenQuestion);
 * </pre>
 *
 * Will later generate following Html :
 *
 * <pre>
 * {@code
 * <form>
 * What do you like ?
 * <input type="checkbox" name="chocolate" id="anId1" /><label form="anId1">I like chocolate</label>
 * <input type="checkbox" name="coke" id="anId2" /><label form="anId2">I like coke</label>
 * <input type="checkbox" name="nothing" id="anId3" /><label form="anId3">I like nothing</label>
 * </form>
 * }
 * </pre>
 *
 * </p>
 */
public final class CheckBoxGroup extends HtmlLeaf {
    private final LabelPosition position;

    /**
     * <p>
     * Creates a new empty CheckBoxGroup with position of the labels
     * <code>AFTER</code> the checkbox
     * </p>
     * <p>
     * Example (after adding a field): <br />
     * {@code <input type="checkbox" name="nothin" id="anId3" /><label form="anId3">I like nothing</label>}
     * </p>
     *
     * @see #CheckBoxGroup(LabelPosition)
     * @see LabelPosition
     */
    public CheckBoxGroup() {
        super();
        this.position = LabelPosition.AFTER;
    }

    /**
     * <p>
     * Creates an empty group of checkbox with a given position of labels
     * relative to the check box
     * </p>
     * <p>
     * Example (after adding a field) of <code>LabelPosition.BEFORE</code> :<br />
     * {@code <label form="anId3">I like nothing</label><input type="checkbox" name="nothin" id="anId3" />}
     * </p>
     * <p>
     * Example (after adding a field) of <code>LabelPosition.AFTER</code> :<br />
     * {@code <input type="checkbox" name="nothin" id="anId3" /><label form="anId3">I like nothing</label>}
     * </p>
     *
     * @param position the relative position of the Labels compared to the
     *            checkBoxes
     */
    public CheckBoxGroup(final LabelPosition position) {
        super();
        this.position = position;
    }

    /**
     * Adds a new checkbox to the group
     *
     * @param name the name of the checkbox (html attribute name)
     * @param label the html {@code <label>} value that will be linked with the
     *            checkbox
     * @return the CheckBox added
     */
    public HtmlCheckbox addCheckBox(final String name, final String label) {
        final HtmlCheckbox box = new HtmlCheckbox(name, label, position);
        add(box);
        return box;
    }
}
