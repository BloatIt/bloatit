/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * service: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.oauth2;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTranslation;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ExternalService;
import com.bloatit.model.ExternalServiceMembership;
import com.bloatit.model.Member;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.OAuthPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("oauth_page")
public final class OAuthPage extends LoggedElveosPage {

    private final OAuthPageUrl url;

    public OAuthPage(final OAuthPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return "Manage your OAuth services";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateFeatureCreationForm(loggedUser));
        // layout.addRight();
        // layout.addRight();
        return layout;
    }

    private HtmlElement generateFeatureCreationForm(final Member me) {

        PlaceHolderElement all = new PlaceHolderElement();

        final HtmlTitleBlock authorizedApplication = new HtmlTitleBlock(Context.tr("These applications can access your Elveos account"), 1);
        PageIterable<ExternalServiceMembership> externalServices = me.getExternalServices();
        for (ExternalServiceMembership serviceMembership : externalServices) {
            ExternalService service = serviceMembership.getService();
            DaoTranslation translation = service.getDescription().getDefaultTranslation();

            HtmlBranch logo = new HtmlDiv();
            if (service.getLogo() != null) {
                logo = new HtmlDiv().add(new HtmlImage(new FileResourceUrl(service.getLogo()), service.getLogo().getShortDescription(), "logo"));
            }
            HtmlBranch description = new HtmlDiv().add(new HtmlTitleBlock(translation.getTitle(), 2).add(new HtmlParagraph(translation.getText()))
                                                                                                    .add(new HtmlParagraph(String.valueOf(serviceMembership.isValid()))));
            HtmlBranch revoke = new HtmlDiv().add(new HtmlLink("TODO", Context.tr("Revoke permissions")))
                                             .add(new HtmlParagraph(Context.getLocalizator()
                                                                           .getDate(serviceMembership.getExpirationDate())
                                                                           .toString(FormatStyle.MEDIUM)));
            // serviceMembership.getLevels();

            authorizedApplication.add(new HtmlDiv().add(logo).add(description).add(revoke));
        }

        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(Context.tr("Manage your created applications"), 1);

        for (ExternalServiceMembership serviceMembership : externalServices) {
            ExternalService service = serviceMembership.getService();
            DaoTranslation translation = service.getDescription().getDefaultTranslation();

            HtmlBranch logo = new HtmlDiv();
            if (service.getLogo() != null) {
                logo = new HtmlDiv().add(new HtmlImage(new FileResourceUrl(service.getLogo()), service.getLogo().getShortDescription(), "logo"));
            }
            HtmlBranch description = new HtmlDiv().add(new HtmlTitleBlock(translation.getTitle(), 2).add(new HtmlParagraph(translation.getText()))
                                                                                                    .addText(service.getToken()));
            // serviceMembership.getLevels();

            createFeatureTitle.add(new HtmlDiv().add(logo).add(description));
        }

        return all.add(authorizedApplication).add(createFeatureTitle);
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to manage your external services.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return OAuthPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new OAuthPageUrl().getHtmlLink(tr("Manage external services")));
        return breadcrumb;
    }
}
