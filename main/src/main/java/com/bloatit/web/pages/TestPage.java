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
package com.bloatit.web.pages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.CheckBoxGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlButton;
import com.bloatit.framework.webprocessor.components.form.HtmlDateField;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNonEscapedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.url.TestPageUrl;

/**
 * A page to test various Html elements
 */
@ParamContainer("test")
public final class TestPage extends ElveosPage {

    public TestPage(final TestPageUrl testPageUrl) {
        super(testPageUrl);
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(tr("Html testing page"), 1);
        pageTitle.add(new HtmlTitleBlock(tr("Common markups"), 2).add(variousElements()));
        pageTitle.add(new HtmlTitleBlock(tr("Formulaires"), 2).add(generateForm()));
        pageTitle.add(new HtmlTitleBlock(tr("Listes"), 2).add(generateLists()));
        return pageTitle;
    }

    private HtmlElement variousElements() {
        final HtmlDiv div = new HtmlDiv();
        final HtmlDiv anotherDiv = new HtmlDiv();
        div.add(anotherDiv);

        final HtmlTitleBlock htb = new HtmlTitleBlock(tr("Another title"), 3);
        final RandomString rs = new RandomString(10);
        final StringBuilder plop = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            plop.append(rs.nextString());
            plop.append(' ');
        }
        htb.add(new HtmlParagraph(plop.toString()));
        anotherDiv.add(htb);
        htb.add(new HtmlTitle("nested title", 5));
        htb.add(new HtmlText(tr("some text without paragraph")));
        htb.add(new HtmlParagraph(new HtmlNonEscapedText("and some with a span <span style=\"font-size: 16pt ; color: fuchsia ; background-color: #FFFFFF ;\">plop</span>")));

        return div;
    }

    private HtmlElement generateForm() {
        final HtmlForm form = new HtmlForm("plop");

        final HtmlFormBlock block1 = new HtmlFormBlock(tr("First form block"));
        block1.add(new HtmlTextField(tr("text", "Field 1")));
        block1.add(new HtmlTextArea("textarea", "Field 2", 10, 20));
        block1.add(new HtmlButton(tr("Useless button")));

        final HtmlFormBlock block2 = new HtmlFormBlock("second form block");
        block2.add(new HtmlDateField("date", "Field 1", Context.getLocalizator().getLocale()));
        block2.add(new HtmlPasswordField("password", "Field 2").setId("Thomas"));

        final HtmlFormBlock block3 = new HtmlFormBlock(tr("Quand êtes vous disponibles ?"));
        final CheckBoxGroup cbg = new CheckBoxGroup(LabelPosition.AFTER);
        cbg.addCheckBox("demain", "demain");
        cbg.addCheckBox("ajd", "aujourd'hui");
        cbg.addCheckBox("hier", "hier");
        block3.add(cbg);
        block3.add(new HtmlParagraph("* test"));

        final HtmlFormBlock block4 = new HtmlFormBlock("Another box");
        final HtmlRadioButtonGroup rbg = new HtmlRadioButtonGroup("test", LabelPosition.BEFORE);
        rbg.addRadioButton("plop", tr("muahahah"));
        rbg.addRadioButton("plip", tr("c'est vraiment chiant de générer des tests"));
        rbg.addRadioButton("plup", tr("et de 3"));
        block4.add(rbg);
        block4.add(new HtmlTextField("another", tr("yet another text field")));

        form.add(block1);
        form.add(block2);
        form.add(block3);
        form.add(block4);
        form.add(new HtmlSubmit("submit"));

        return form;
    }

    private HtmlElement generateLists() {
        final HtmlDiv lists = new HtmlDiv();
        final HtmlTitleBlock std = new HtmlTitleBlock(tr("Not numbered list"), 3);
        final HtmlList list = new HtmlList();
        std.add(list);
        list.add("plop");
        list.add(new HtmlLink("plop", "hello"));
        list.add("another one");

        final HtmlTitleBlock nbm = new HtmlTitleBlock(tr("Numbered list"), 3);
        final HtmlList nbList = new HtmlList(HtmlList.ListType.NUMBERED);
        nbm.add(nbList);
        nbList.add("plop");
        nbList.add(new HtmlLink("plop", "hello"));
        nbList.add("another one");

        lists.add(std);
        lists.add(nbm);
        return lists;
    }

    @Override
    protected String createPageTitle() {
        return "test page";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return TestPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new TestPageUrl().getHtmlLink(tr("Test")));

        return breadcrumb;
    }
}
