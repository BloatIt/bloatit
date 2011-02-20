/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework.webserver.components.form;

/**
 * <p>
 * Class used to create a file uploading box
 * </p>
 * <p>
 * <b>NOTE</b>: When using a <code>HtmlFileInput</code> it is usually wise to use the
 * {@link HtmlForm#enableFileUpload()}, otherwise the file will not be uploaded.
 * </p>
 * 
 * @see HtmlForm#enableFileUpload()
 */
public final class HtmlFileInput extends HtmlFormField<String> {

    /**
     * Creates a file input with a given name
     * 
     * @param name the name used in the html attribute <code>name</code>
     */
    public HtmlFileInput(final String name) {
        super(new HtmlSimpleInput("file"), name);
    }

    /**
     * Creates a file input with a given name and some displayed text
     * 
     * @param name the name used in the html attribute <code>name</code>
     * @param label the text displayed inside the {@code <label>} markup
     */
    public HtmlFileInput(final String name, final String label) {
        super(new HtmlSimpleInput("file"), name, label);
    }

    /**
     * Creates a file input based on some form field data
     * 
     * @param data the data used to generate the field
     * @param label the text displayed inside the {@code <label>} markup
     */
    public HtmlFileInput(final FormFieldData<String> data, final String label) {
        super(new HtmlSimpleInput("file"), data.getFieldName(), label);
        setDefaultValue(data);

        addErrorMessages(data.getFieldMessages());
    }

    @Override
    protected void doSetDefaultValue(final String value) {
        addAttribute("value", value);
    }
}
