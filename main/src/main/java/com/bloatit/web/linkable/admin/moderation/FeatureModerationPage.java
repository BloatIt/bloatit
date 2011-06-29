package com.bloatit.web.linkable.admin.moderation;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.FeatureTabPane;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.FeatureModerationActionUrl;
import com.bloatit.web.url.FeatureModerationPageUrl;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * This page is called by links present directly INSIDE the elveos website. It
 * cannot be reached from the administration section of the site
 */
@ParamContainer("moderatefeature/%feature%")
public class FeatureModerationPage extends AdminPage {
    private FeatureModerationPageUrl url;

    @NonOptional(@tr("You have to specify the feature you want to delete."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    private final Feature feature;

    public FeatureModerationPage(FeatureModerationPageUrl url) {
        super(url);
        this.url = url;
        this.feature = url.getFeature();
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        TwoColumnLayout master = new TwoColumnLayout(true, url);
        master.addLeft(new HtmlTitle(Context.tr("Feature moderation"), 1));

        HtmlParagraph warning = new HtmlParagraph();
        warning.addText(Context.tr("Are you sure you want to delete this feature ?"));
        master.addLeft(warning);

        HtmlLink ok = new FeatureModerationActionUrl(getSession().getShortKey(), feature).getHtmlLink(Context.tr("Delete"));
        ok.setCssClass("button");
        HtmlLink cancel = new FeaturePageUrl(feature, FeatureTabPane.TabKey.description).getHtmlLink(Context.tr("Cancel"));
        cancel.setCssClass("button");
        
        master.addLeft(ok);
        master.addLeft(cancel);

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb(Member member) {
        Breadcrumb br = FeaturePage.generateBreadcrumb(feature);
        br.pushLink(new FeatureModerationPageUrl(feature).getHtmlLink("Moderate"));
        return br;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Feature moderation page");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
