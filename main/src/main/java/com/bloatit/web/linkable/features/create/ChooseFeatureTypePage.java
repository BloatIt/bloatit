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
package com.bloatit.web.linkable.features.create;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ChooseFeatureTypePageUrl;
import com.bloatit.web.url.CreateFeatureAndOfferPageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page use to choose if the user want develop the feature
 */
@ParamContainer("feature/create/choosetype")
public final class ChooseFeatureTypePage extends LoggedElveosPage {

    private final ChooseFeatureTypePageUrl url;

    public ChooseFeatureTypePage(final ChooseFeatureTypePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Create new feature");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final HtmlTitleBlock title = new HtmlTitleBlock(tr("Create a feature"), 1);
        HtmlDiv chooseFeatureTypeBlock = new HtmlDiv("choose_feature_type");

        final HtmlTitleBlock chooseTypeTitle = new HtmlTitleBlock(tr("Do you want to implement the feature yourself ?"), 2);

        // Create the first form
        final HtmlForm developForm = new HtmlForm(new CreateFeatureAndOfferPageUrl().urlString());
        developForm.add(new HtmlSubmit(tr("Yes, I want to implement the feature")));
        developForm.add(new HtmlParagraph(Context.tr("You are a developer. You have an idea for your software and you want to fund it.")));
        chooseTypeTitle.add(developForm);

        // Create the seconde form
        final HtmlForm dontDevelopForm = new HtmlForm(new CreateFeaturePageUrl().urlString());
        dontDevelopForm.add(new HtmlSubmit(tr("No, I just want to submit it")));
        dontDevelopForm.add(new HtmlParagraph(Context.tr("You use a free software and you have an enhancement idea to submit.")));
        chooseTypeTitle.add(dontDevelopForm);

        chooseFeatureTypeBlock.add(chooseTypeTitle);

        layout.addLeft(title);
        layout.addLeft(chooseFeatureTypeBlock);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_feature"));
        layout.addRight(new SidebarMarkdownHelp());

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new feature.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return ChooseFeatureTypePage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();
        breadcrumb.pushLink(new CreateFeaturePageUrl().getHtmlLink(tr("Create a feature")));
        return breadcrumb;
    }
}
