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

import static com.bloatit.framework.webserver.Context.tr;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.model.Feature;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.FeaturePageUrl;

@ParamContainer("feature")
public final class FeaturePage extends MasterPage {

    public static final String FEATURE_FIELD_NAME = "id";

    @RequestParam(name = FEATURE_FIELD_NAME)
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
    protected String getPageTitle() {
        if (feature != null) {
            try {
                return feature.getTitle();
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying feature name. Please notify us."));
                throw new ShallNotPassException("User cannot access feature name", e); 
            }
        }
        return tr("Feature not found !");
    }

    @Override
    protected List<String> getCustomCss() {
        ArrayList<String> custom = new ArrayList<String>();
        custom.add("feature.css");
        return custom;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    protected void doCreate() throws RedirectException {
        addNotifications(url.getMessages());
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }
        // The feature page is composed of 3 parts:
        // - The sumary
        // - The tab panel
        // - The comments

        TwoColumnLayout layout = new TwoColumnLayout(false);


        layout.addLeft(new FeatureSummaryComponent(feature));
        layout.addLeft(new FeatureTabPane(url.getFeatureTabPaneUrl(), feature));
        layout.addLeft(new FeatureCommentListComponent(feature));

        layout.addRight(new SideBarDocumentationBlock("feature"));
        add(layout);

    }


    public static Breadcrumb generateBreadcrumb(Feature feature) {
        Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();

        FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);

        try {
            breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Feature for {0}", feature.getSoftware().getName())));
        } catch (UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying feature information. Please notify us."));
            throw new ShallNotPassException("User cannot access feature information", e); 
        }

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbBugs(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.BUGS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.BUGS_TAB);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Bugs")));

        return breadcrumb;
    }


    public static Breadcrumb generateBreadcrumbOffers(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.OFFERS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.OFFERS_TAB);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Offers")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbContributions(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.CONTRIBUTIONS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.CONTRIBUTIONS_TAB);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Contributions")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbDetails(Feature feature) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature);
        featurePageUrl.getFeatureTabPaneUrl().setActiveTabKey(FeatureTabPane.DETAILS_TAB);
        featurePageUrl.setAnchor(FeatureTabPane.DETAILS_TAB);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Details")));

        return breadcrumb;
    }


    @Override
    protected Breadcrumb getBreadcrumb() {
        if(url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.BUGS_TAB)) {
            return FeaturePage.generateBreadcrumbBugs(feature);
        }
        if(url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.CONTRIBUTIONS_TAB)) {
            return FeaturePage.generateBreadcrumbContributions(feature);
        }
        if(url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.DETAILS_TAB)) {
            return FeaturePage.generateBreadcrumbDetails(feature);
        }
        if(url.getFeatureTabPaneUrl().getActiveTabKey().equals(FeatureTabPane.OFFERS_TAB)) {
            return FeaturePage.generateBreadcrumbOffers(feature);
        }

        return FeaturePage.generateBreadcrumb(feature);
    }

}
