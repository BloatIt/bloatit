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

import org.springframework.web.util.HtmlUtils;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.SubParamContainer;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureTabPane.TabKey;
import com.bloatit.web.linkable.usercontent.CommentForm;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.pages.tools.CommentTools;
import com.bloatit.web.url.CreateCommentActionUrl;
import com.bloatit.web.url.FeaturePageUrl;

@ParamContainer("features/%feature%/%activeTabKey#getFeatureTabPaneUrl().getActiveTabKeyParameter().getStringValue()%")
public final class FeaturePage extends ElveosPage {

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("You have to specify a feature number."))
    private final Feature feature;

    // Sub component.
    @SuppressWarnings("unused")
    @SubParamContainer
    private FeatureTabPane featureTabPane;

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PRETTY, generatedFrom = "feature")
    @Optional("Title")
    private final String title;

    private final FeaturePageUrl url;

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

    public Feature getFeature() {
        return feature;
    }

    /**
     * The feature page body content is composed of 3 parts : <li>The summary</li>
     * <li>The tab panel</li> <li>The comments</li>
     */
    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(false, url);

        layout.addLeft(new FeatureSummaryComponent(feature));
        layout.addLeft(new FeatureTabPane(url.getFeatureTabPaneUrl(), feature));

        final HtmlDiv commentsBlock = new HtmlDiv("comments_block", "comments_block");
        {
            commentsBlock.add(new HtmlTitleBlock(Context.tr("Comments ({0})", feature.getCommentsCount()), 1).setCssClass("comments_title"));
            commentsBlock.add(CommentTools.generateCommentList(feature.getComments()));
            commentsBlock.add(new CommentForm(new CreateCommentActionUrl(getSession().getShortKey(), feature)));
        }
        layout.addLeft(commentsBlock);

        layout.addRight(new SideBarDocumentationBlock("feature"));
        layout.addRight(new SideBarElveosButtonBlock(feature));

        return layout;
    }

    public static Breadcrumb generateBreadcrumb(final Feature feature) {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();
        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, TabKey.description);
        if (feature.getSoftware() != null) {
            breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Feature for {0}", feature.getSoftware().getName())));
        } else {
            breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Feature {0}", String.valueOf(feature.getId()))));
        }
        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbBugs(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, TabKey.bugs);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Bugs")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbOffers(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, TabKey.offers);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Offers")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbContributions(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, TabKey.contributions);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Contributions")));

        return breadcrumb;
    }

    private static Breadcrumb generateBreadcrumbDetails(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumb(feature);

        final FeaturePageUrl featurePageUrl = new FeaturePageUrl(feature, TabKey.details);
        featurePageUrl.setAnchor(FeatureTabPane.FEATURE_TAB_PANE);

        breadcrumb.pushLink(featurePageUrl.getHtmlLink(tr("Details")));

        return breadcrumb;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        if (url.getFeatureTabPaneUrl().getActiveTabKey() == TabKey.bugs) {
            return FeaturePage.generateBreadcrumbBugs(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey() == TabKey.contributions) {
            return FeaturePage.generateBreadcrumbContributions(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey() == TabKey.details) {
            return FeaturePage.generateBreadcrumbDetails(feature);
        }
        if (url.getFeatureTabPaneUrl().getActiveTabKey() == TabKey.offers) {
            return FeaturePage.generateBreadcrumbOffers(feature);
        }

        return FeaturePage.generateBreadcrumb(feature);
    }

    @Override
    protected String createPageTitle() {
        if (feature != null) {
            if (feature.getSoftware() == null) {
                return feature.getTitle();
            } else {
                return Context.tr("{0} - {1}", feature.getTitle(), feature.getSoftware());
            }
        }
        return Context.tr("feature not found.");
    }

    @Override
    protected String getPageDescription() {
        String title = feature.getTitle();
        if (title.endsWith(".") || title.endsWith(":") || title.endsWith("!") || title.endsWith("?")) {
            title = title.substring(0, title.length() - 1);
        }
        String str = null;
        if (feature.getSoftware() != null) {
            str = Context.tr("Elveos the open source collaborative financing website proposes you to finance the creation of: {1} - {0}",
                             title,
                             feature.getSoftware().getName());
        } else {
            str = Context.tr("Elveos the open source collaborative financing website proposes you to finance the creation of: {0}", title);
        }
        return HtmlUtils.htmlEscape(str);
    }
}
