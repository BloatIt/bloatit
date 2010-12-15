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
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.CheckBoxGroup;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlDateField;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlFormBlock;
import com.bloatit.web.html.components.standard.form.HtmlFormField.LabelPosition;
import com.bloatit.web.html.components.standard.form.HtmlPasswordField;
import com.bloatit.web.html.components.standard.form.HtmlRadioButtonGroup;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.RandomString;

/**
 * A page to test various Html elements
 */
public class TestPage extends Page {

    public TestPage() throws RedirectException {
        super();
    }

    @Override
    public void create() throws RedirectException {
        super.create();

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock("Html testing page", 1);
        pageTitle.add(new HtmlTitleBlock("Common markups", 2).add(variousElements()));
        pageTitle.add(new HtmlTitleBlock("Formulaires", 2).add(generateForm()));
        pageTitle.add(new HtmlTitleBlock("Listes", 2).add(generateLists()));
        add(pageTitle);
    }

    private HtmlElement variousElements() {
        final HtmlDiv div = new HtmlDiv();
        final HtmlDiv anotherDiv = new HtmlDiv();
        div.add(anotherDiv);

        final HtmlTitleBlock htb = new HtmlTitleBlock("Another title", 3);
        final RandomString rs = new RandomString(10);
        String plop = "";
        for (int i = 0; i < 100; i++) {
            plop += rs.nextString() + " ";
        }
        htb.add(new HtmlParagraph(plop));
        anotherDiv.add(htb);
        htb.add(new HtmlTitle("nested title", 5));
        htb.add(new HtmlText("some text without paragraph"));
        htb.add(new HtmlParagraph(new HtmlText(
                "and some with a span <span style=\"font-size: 16pt ; color: fuchsia ; background-color: #FFFFFF ;\">plop</span>")));

        return div;
    }

    private HtmlElement generateForm() {
        final HtmlForm form = new HtmlForm("plop");

        final HtmlFormBlock block1 = new HtmlFormBlock("First form block");
        block1.add(new HtmlTextField("text", "Field 1"));
        block1.add(new HtmlTextArea("textarea", "Field 2", 10, 20));
        block1.add(new HtmlButton("Useless button"));

        final HtmlFormBlock block2 = new HtmlFormBlock("second form block");
        block2.add(new HtmlDateField("date", "Field 1"));
        block2.add(new HtmlPasswordField("password", "Field 2").setId("Thomas"));

        final HtmlFormBlock block3 = new HtmlFormBlock("Quand êtes vous disponibles ?");
        final CheckBoxGroup cbg = new CheckBoxGroup(LabelPosition.AFTER);
        cbg.addCheckBox("demain", "demain");
        cbg.addCheckBox("ajd", "aujourd'hui");
        cbg.addCheckBox("hier", "hier");
        block3.add(cbg);
        block3.add(new HtmlParagraph("* test"));

        final HtmlFormBlock block4 = new HtmlFormBlock("Another box");
        final HtmlRadioButtonGroup rbg = new HtmlRadioButtonGroup("test", LabelPosition.BEFORE);
        rbg.addRadioButton("plop", "muahahah");
        rbg.addRadioButton("plip", "c'est vraiment chiant de générer des tests");
        rbg.addRadioButton("plup", "et de 3");
        block4.add(rbg);
        block4.add(new HtmlTextField("another", "yet another text field"));

        form.add(block1);
        form.add(block2);
        form.add(block3);
        form.add(block4);
        form.add(new HtmlSubmit("submit"));

        return form;
    }

    private HtmlElement generateLists() {
        final HtmlDiv lists = new HtmlDiv();
        final HtmlTitleBlock std = new HtmlTitleBlock("Not numbered list", 3);
        final HtmlList list = new HtmlList();
        std.add(list);
        list.add("plop");
        list.add(new HtmlLink("plop", "hello"));
        list.add("another one");

        final HtmlTitleBlock nbm = new HtmlTitleBlock("Numbered list", 3);
        final HtmlList nbList = new HtmlList(HtmlList.listType.NUMBERED);
        nbm.add(nbList);
        nbList.add("plop");
        nbList.add(new HtmlLink("plop", "hello"));
        nbList.add("another one");

        lists.add(std);
        lists.add(nbm);
        return lists;
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
