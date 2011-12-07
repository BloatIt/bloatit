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
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlEmailField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.LostPasswordActionUrl;
import com.bloatit.web.url.LostPasswordPageUrl;

/**
 * Page part of the password recovery process.
 * <p>
 * This page is the first page in the process. It is shown when the user clicks
 * on "lost password"
 * </p>
 */
@ParamContainer(value = "member/password/lost", protocol = Protocol.HTTPS)
public class LostPasswordPage extends ElveosPage {
    private final LostPasswordPageUrl url;

    public LostPasswordPage(final LostPasswordPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Password recovery"), 1);
        layout.addLeft(master);

        final LostPasswordActionUrl targetUrl = new LostPasswordActionUrl();
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        master.add(form);

        FormBuilder ftool = new FormBuilder(LostPasswordAction.class, targetUrl);
        ftool.add(form, new HtmlEmailField(targetUrl.getEmailParameter().getName()));
        form.addSubmit(new HtmlSubmit(Context.tr("Submit")));
        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Recover password");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
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
