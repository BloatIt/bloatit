//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.login;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.LostPasswordActionUrl;
import com.bloatit.web.url.LostPasswordPageUrl;

/**
 * Page part of the password recovery process.
 * <p>
 * This page is the first page in the process. It is shown when the user clicks
 * on "lost password"
 * </p>
 */
@ParamContainer(value="password/lost", protocol=Protocol.HTTPS)
public class LostPasswordPage extends ElveosPage {
    private final LostPasswordPageUrl url;

    public LostPasswordPage(LostPasswordPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent(ElveosUserToken userToken) throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true, url);

        HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Password recovery"), 1);
        layout.addLeft(master);

        LostPasswordActionUrl targetUrl = new LostPasswordActionUrl();
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.add(form);

        // EMAIL
        FieldData emailFieldData = targetUrl.getEmailParameter().pickFieldData();
        HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), Context.tr("Enter your email"));
        emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
        form.add(emailInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));
        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Recover password");
    }

    @Override
    protected Breadcrumb createBreadcrumb(ElveosUserToken userToken) {
        return generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = LoginPage.generateBreadcrumb();
        breadcrumb.pushLink(new LostPasswordPageUrl().getHtmlLink(tr("Recover password")));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
