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
package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.FeaturePageUrl;

@ParamContainer("feature")
public final class FeaturePage extends MasterPage {

    @RequestParam(name = "id", conversionErrorMsg = @tr("I cannot find the feature number: ''%value''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a feature number."))
    private final Feature feature;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "feature")
    @Optional("Title")
    private final String title;

    private final FeaturePageUrl url;

    @SuppressWarnings("unused")
    private FeatureTabPane featureTabPane;

    public FeaturePage(final FeaturePageUrl url) {
        super(url);
        this.url = url;
        feature = url.getFeature();
        title = url.getTitle();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String createPageTitle() {
        if (feature != null) {
            try {
                return feature.getTitle();
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying feature name. Please notify us."));
                throw new ShallNotPassException("User cannot access feature name", e);
            }
        }
        return Context.tr("feature not found.");
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        // The feature page is composed of 3 parts:
        // - The summary
        // - The tab panel
        // - The comments

        final TwoColumnLayout layout = new TwoColumnLayout(false, url);

        layout.addLeft(new FeatureSummaryComponent(feature));
        layout.addLeft(new FeatureTabPane(url.getFeatureTabPaneUrl(), feature));
        layout.addLeft(new FeatureCommentListComponent(feature));

        layout.addRight(new SideBarDocumentationBlock("feature"));

        return layout;
    }

    public static Breadcrumb generateBreadcrumb(final Feature feature) {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);

        try {
            breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Feature for {0}", feature.getSoftware().getName())));
        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying feature information. Please notify us."));
            throw new ShallNotPassException("User cannot access feature information", e);
        }

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbBugs(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.BUGS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Bugs")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbOffers(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.OFFERS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Offers")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbContributions(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.CONTRIBUTIONS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Contributions")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbDetails(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.DETAILS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Details")));

        return breadcrumb;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        if (url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.BUGS_TAB)) {
            return FeaturePage.generateBreadcrumbBugs(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.CONTRIBUTIONS_TAB)) {
            return FeaturePage.generateBreadcrumbContributions(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.DETAILS_TAB)) {
            return FeaturePage.generateBreadcrumbDetails(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.OFFERS_TAB)) {
            return FeaturePage.generateBreadcrumbOffers(feature);
        }

        return FeaturePage.generateBreadcrumb(feature);
    }
}
