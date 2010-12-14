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
package com.bloatit.web.html.pages;

import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlDateField;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlFormBlock;
import com.bloatit.web.html.components.standard.form.HtmlPasswordField;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;

/**
 * A page to test various Html elements
 */
public class TestPage extends Page {

    public TestPage(final Request request) throws RedirectException {
        super(request);
    }

    @Override
    public void create() throws RedirectException {
        super.create();

        HtmlTitleBlock pageTitle = new HtmlTitleBlock("Html testing page", 1);
        pageTitle.add(generateForm());
        add(pageTitle);
    }

    private HtmlElement generateForm() {
        HtmlForm form = new HtmlForm("");

        HtmlFormBlock block1 = new HtmlFormBlock("First form block");
        block1.add(new HtmlTextField("test", "Field 1"));
        block1.add(new HtmlTextArea("test2","Field 2"));

        HtmlFormBlock block2 = new HtmlFormBlock("First form block");
        block2.add(new HtmlDateField("test", "Field 1"));
        block2.add(new HtmlPasswordField("test2","Field 2"));

        form.add(block1);
        form.add(block2);
        return form;
    }

    @Override
    protected String getTitle() {
        return "test page";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
