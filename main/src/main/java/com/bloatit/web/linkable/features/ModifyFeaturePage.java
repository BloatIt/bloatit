package com.bloatit.web.linkable.features;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.create.CreateFeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.ModifyFeatureActionUrl;
import com.bloatit.web.url.ModifyFeaturePageUrl;

@ParamContainer("modifyfeature/%feature%")
public class ModifyFeaturePage extends LoggedElveosPage {

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("You have to specify a feature number."))
    private final Feature feature;

    private final ModifyFeaturePageUrl url;

    public ModifyFeaturePage(ModifyFeaturePageUrl url) {
        super(url);
        this.url = url;
        this.feature = url.getFeature();
    }

    @Override
    public HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException {
        if (!feature.canModify()) {
            throw new RedirectException(new FeaturePageUrl(feature, FeatureTabKey.description));
        }
        TwoColumnLayout master = new TwoColumnLayout(true, url);
        master.addLeft(new HtmlTitle(Context.tr("Modify feature page"), 1));

        ModifyFeatureActionUrl targetUrl = new ModifyFeatureActionUrl(Context.getSession().getShortKey(), feature);
        HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        master.addLeft(form);
        FormBuilder ftool = new FormBuilder(ModifyFeatureAction.class, targetUrl);

        // Title of the feature
        HtmlTextField title = new HtmlTextField(targetUrl.getTitleParameter().getName());
        ftool.add(form, title);
        ftool.setDefaultValueIfNeeded(title, feature.getDescription().getTranslation(feature.getDescription().getDefaultLanguage()).getTitle());

        // Description of the feature
        MarkdownEditor description = new MarkdownEditor(targetUrl.getDescriptionParameter().getName(),
                                                        CreateFeaturePage.SPECIF_INPUT_NB_LINES,
                                                        CreateFeaturePage.SPECIF_INPUT_NB_COLUMNS);
        ftool.add(form, description);
        ftool.setDefaultValueIfNeeded(description, feature.getDescription().getTranslation(feature.getDescription().getDefaultLanguage()).getText());

        // Linked software
        final FieldData newSoftwareNameFD = targetUrl.getNewSoftwareNameParameter().pickFieldData();
        final FieldData newSoftwareFD = targetUrl.getNewSoftwareParameter().pickFieldData();
        final SoftwaresTools.SoftwareChooserElement software = new SoftwaresTools.SoftwareChooserElement(targetUrl.getSoftwareParameter().getName(),
                                                                                                         newSoftwareNameFD.getName(),
                                                                                                         newSoftwareFD.getName());
        ftool.add(form, software);
        if (feature.getSoftware() != null) {
            software.setDefaultValue(feature.getSoftware().getId().toString());
        }
        if (newSoftwareNameFD.getSuggestedValue() != null) {
            software.setNewSoftwareDefaultValue(newSoftwareNameFD.getSuggestedValue());
        }
        if (newSoftwareFD.getSuggestedValue() != null) {
            software.setNewSoftwareCheckboxDefaultValue(newSoftwareFD.getSuggestedValue());
        }

        form.addSubmit(new HtmlSubmit(Context.tr("Modify the feature")));

        master.addRight(new SidebarMarkdownHelp());

        return master;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to modify a feature.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(Member loggedUser) {
        Breadcrumb br = FeaturePage.generateBreadcrumb(feature);
        br.pushLink(url.getHtmlLink(Context.tr("modify")));
        return br;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Modify feature");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
