package com.bloatit.web.linkable.usercontent;

import java.util.Locale;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CreateUserContentActionUrl;

@ParamContainer("usercontent/docreate")
public abstract class CreateUserContentAction extends LoggedAction {

    @RequestParam(role = Role.POST)
    @Optional
    private final Team team;

    @RequestParam(role = Role.POST)
    @ParamConstraint(length = 2, //
                     LengthErrorMsg = @tr("Language ''%value'' not found. Are you sure you haven't messed up with this page parameters?"))
    @Optional
    private final String locale;

    @RequestParam(name = "attachement", role = Role.POST)
    @Optional
    private final String attachement;

    @RequestParam(name = "attachement/filename", role = Role.POST)
    @Optional
    private final String attachementFileName;

    @RequestParam(name = "attachement_description", role = Role.POST)
    @Optional
    private final String attachementDescription;

    @RequestParam(name = "attachement/contenttype", role = Role.POST)
    @Optional
    private final String attachementContentType;

    private FileMetadata file;

    private final CreateUserContentActionUrl createUserActionurl;

    public CreateUserContentAction(final CreateUserContentActionUrl url) {
        super(url);
        this.createUserActionurl = url;
        team = url.getTeam();
        locale = url.getLocale();
        attachement = url.getAttachement();
        attachementFileName = url.getAttachementFileName();
        attachementDescription = url.getAttachementDescription();
        attachementContentType = url.getAttachementContentType();

        file = null;
    }

    @Override
    protected final Url doProcessRestricted(final Member authenticatedMember) {
        if (attachement != null) {
            if (verifyFile(attachement)) {
                file = FileMetadataManager.createFromTempFile(authenticatedMember, attachement, attachementFileName, attachementDescription);
            } else {
                return doProcessErrors();
            }
        }
        return doDoProcessRestricted(authenticatedMember);
    }

    @Override
    protected final void transmitParameters() {
        session.addParameter(createUserActionurl.getTeamParameter());
        session.addParameter(createUserActionurl.getLocaleParameter());
        // TODO do we have to save the file ?
        doTransmitParameters();
    }

    protected abstract void doTransmitParameters();

    protected abstract Url doDoProcessRestricted(Member authenticatedMember);

    protected abstract boolean verifyFile(String filename);

    protected final Team getTeam() {
        return team;
    }

    protected final Locale getLocale() {
        if (locale != null && locale.length() == 2) {
            return new Locale(locale);
        }
        return null;
    }

    protected final String getAttachement() {
        return attachement;
    }

    protected final String getAttachementFileName() {
        return attachementFileName;
    }

    protected final String getAttachementDescription() {
        return attachementDescription;
    }

    protected final String getAttachementContentType() {
        return attachementContentType;
    }

    protected final FileMetadata getFile() {
        return file;
    }
}
