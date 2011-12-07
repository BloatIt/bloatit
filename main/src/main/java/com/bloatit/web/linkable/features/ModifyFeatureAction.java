package com.bloatit.web.linkable.features;

import java.util.Locale;

import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.form.FormComment;
import com.bloatit.framework.webprocessor.components.form.FormField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.ModifyFeatureActionUrl;
import com.bloatit.web.url.ModifyFeaturePageUrl;

@ParamContainer("domodifyfeature/%feature%")
public class ModifyFeatureAction extends LoggedElveosAction {

    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("You have to specify a feature number."))
    private final Feature feature;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a title"))
    @MinConstraint(min = 10, message = @tr("The title must have at least %constraint% chars."))
    @MaxConstraint(max = 80, message = @tr("The title must be %constraint% chars length max."))
    @FormField(label = @tr("Title"), isShort = false)
    @FormComment(@tr("The title length must be at least 10 chars long."))
    private final String title;

    @NonOptional(@tr("You forgot to write a description"))
    @MinConstraint(min = 10, message = @tr("The description must have at least %constraint% chars."))
    @MaxConstraint(max = 800000, message = @tr("The description must be %constraint% chars length max."))
    @RequestParam(role = Role.POST)
    @FormField(label = @tr("Description"), isShort = false)
    @FormComment(@tr("Try to be the most specific possible."))
    private String description;

    @Optional
    @RequestParam(role = Role.POST)
    @FormField(label = @tr("Software"))
    private final Software software;

    @Optional
    @RequestParam(role = Role.POST)
    private final String newSoftwareName;

    @RequestParam(role = Role.POST)
    @Optional
    private final Boolean newSoftware;

    private final ModifyFeatureActionUrl url;

    public ModifyFeatureAction(ModifyFeatureActionUrl url) {
        super(url);
        this.url = url;
        this.feature = url.getFeature();
        this.description = url.getDescription();
        this.software = url.getSoftware();
        this.title = url.getTitle();
        this.newSoftware = url.getNewSoftware();
        this.newSoftwareName = url.getNewSoftwareName();
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        try {
            feature.setDescription(description, feature.getDescription().getDefaultLanguage());
            if (software == null && newSoftwareName != null && !newSoftwareName.isEmpty()) {
                Software softwareToUse = null;
                try {
                    softwareToUse = new Software(newSoftwareName, me, Locale.ENGLISH, "No description yet.");
                } catch (UniqueNameExpectedException e) {
                    softwareToUse = SoftwareManager.getByName(newSoftwareName);
                }
                if (softwareToUse != null) {
                    feature.setSoftware(softwareToUse);
                }
            } else if (newSoftwareName == null) {
                feature.setSoftware(software);
            } else if (newSoftware != null && newSoftware) {
                feature.setSoftware(null);
            }
            feature.setTitle(title, feature.getDescription().getDefaultLanguage());

        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("User cannot modify a feature, we checked right before tho ...");
        }
        return new FeaturePageUrl(feature, FeatureTabKey.description);
    }

    @Override
    protected Url checkRightsAndEverything(Member me) {
        if (!feature.canModify()) {
            return new FeaturePageUrl(feature, FeatureTabKey.description);
        }
        if (software == null && newSoftwareName != null && newSoftwareName.equals("--invalid--")) {
            session.notifyError(Context.tr("You have to specify a valid software."));
            return new CreateFeaturePageUrl();
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        if (feature != null) {
            return new ModifyFeaturePageUrl(feature);
        }
        return new IndexPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify a feature");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getSoftwareParameter());
        session.addParameter(url.getTitleParameter());
    }
}
