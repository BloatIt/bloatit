package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.create.CreateFeaturePage;
import com.bloatit.web.pages.LoggedElveosPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
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

        ModifyFeatureActionUrl modifyUrl = new ModifyFeatureActionUrl(Context.getSession().getShortKey(), feature);
        HtmlForm modifyForm = new HtmlForm(modifyUrl.urlString());
        master.addLeft(modifyForm);

        // Title of the feature
        final FieldData descriptionFieldData = modifyUrl.getTitleParameter().pickFieldData();
        final HtmlTextField titleInput = new HtmlTextField(descriptionFieldData.getName(), tr("Title"));
        titleInput.addErrorMessages(descriptionFieldData.getErrorMessages());
        titleInput.setCssClass("input_long_400px");
        titleInput.setComment(tr("The title of the new feature must be permit to identify clearly the feature's specificity."));
        if (descriptionFieldData.getSuggestedValue() == null) {
            titleInput.setDefaultValue(feature.getTitle());
        } else {
            titleInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
        }

        modifyForm.add(titleInput);

        // Linked software
        final FieldData softwareFieldData = modifyUrl.getSoftwareParameter().pickFieldData();
        final HtmlDropDown softwareInput = new HtmlDropDown(softwareFieldData.getName(), Context.tr("Software"));

        softwareInput.addDropDownElement("", Context.tr("Select a software")).setDisabled().setSelected();
        softwareInput.addDropDownElement("", Context.tr("New software"));
        for (final Software software : SoftwareManager.getAll()) {
            softwareInput.addDropDownElement(String.valueOf(software.getId()), software.getName());
        }
        softwareInput.setComment(Context.tr("The software of the feature request. Select 'new software' if your feature is the creation of a new software."));
        if (softwareFieldData.getSuggestedValue() != null) {
            softwareInput.setDefaultValue(softwareFieldData.getSuggestedValue());
        } else {
            softwareInput.setDefaultValue(feature.getSoftware().getId().toString());
        }
        modifyForm.add(softwareInput);

        // Description of the feature
        final FieldData specificationFieldData = modifyUrl.getDescriptionParameter().pickFieldData();
        final MarkdownEditor specificationInput = new MarkdownEditor(specificationFieldData.getName(),
                                                                     tr("Enter the new description"),
                                                                     CreateFeaturePage.SPECIF_INPUT_NB_LINES,
                                                                     CreateFeaturePage.SPECIF_INPUT_NB_COLUMNS);

        if (specificationFieldData.getSuggestedValue() == null || specificationFieldData.getSuggestedValue().isEmpty()) {
            specificationInput.setDefaultValue(feature.getDescription().getDefaultTranslation().getText());
        } else {
            specificationInput.setDefaultValue(specificationFieldData.getSuggestedValue());
        }
        specificationInput.addErrorMessages(specificationFieldData.getErrorMessages());
        specificationInput.setComment(tr("Enter a long description of the feature : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        modifyForm.add(specificationInput);

        final MarkdownPreviewer mdPreview = new MarkdownPreviewer(specificationInput);
        modifyForm.add(mdPreview);

        modifyForm.add(new HtmlSubmit(Context.tr("Modify the feature")));

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
