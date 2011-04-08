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

import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;

/**
 * <p>
 * A class used to represent html {@code <form></form>} markup
 * </p>
 * <p>
 * <code>HtmlForm</code> is a container for <code>HtmlFormField</code>.
 * </p>
 * <p>
 * Usage: <br />
 *
 * <pre>
 * HtmlForm form = new HtmlForm("someaction");
 * form.add(new HtmlTextField("login"));
 * form.add(new HtmlDateField("date of birt");
 * form.add(new HtmlSubmit("submit"));
 * </pre>
 *
 * </p>
 * <p>
 * Every form should include a <code>HtmlSubmit</code> otherwise it won't be
 * possible to validate form input.<br />
 * Note, submit can be done via javascript, but should be avoided ...
 * </p>
 */
public class HtmlForm extends HtmlBranch {
    public enum Method {
        /**
         * Results of the form are returned in the URL of the form
         */
        GET,

        /**
         * Results of the form are returned in the POST of the page
         */
        POST
    }

    /**
     * <p>
     * Convenience method to create a form without specifying the send method.
     * The used send method is <code>POST</code>
     * </p>
     * <p>
     * Similar to <code>new HtmlForm(target, Method.POST);</code>
     * </p>
     *
     * @param target the target of the form (used in the <code>action</code>
     *            attribute)
     * @see HtmlForm#HtmlForm(String, Method)
     */
    public HtmlForm(final String target) {
        this(target, Method.POST);
    }

    /**
     * <p>
     * Creates a form with a given target and a given send method
     * </p>
     *
     * @param target the target of the form (used in the <code>action</code>
     *            attribute)
     * @param method the method used to send data, either <code>GET</code> or
     *            <code>POST</code> (<code>POST</code> advised)
     */
    public HtmlForm(final String target, final Method method) {
        super("form");
        addAttribute("action", target);
        addAttribute("method", method == Method.GET ? "get" : "post");
    }

    /**
     * <p>
     * Allows file upload to happen from this form
     * </p>
     * <p>
     * This method will change the encoding used to send the data. It is not
     * advised to use this when not needed as it can be a strain on performances
     * (data is way heavier to parse when it is activated).
     * </p>
     * <p>
     * If an {@link HtmlFileInput} is included in a form where
     * {@link #enableFileUpload()} has <b>not</b> been called, the form will
     * simply return the file url instead of the file content, which shouldn't
     * be very helpful
     * </p>
     *
     * @see HtmlFileInput
     */
    public void enableFileUpload() {
        addAttribute("enctype", "multipart/form-data");
    }
}
