package com.bloatit.web.linkable.usercontent;

import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CreateUserContentActionUrl;

@ParamContainer("usercontent/docreate")
public abstract class CreateUserContentAction extends LoggedAction {

    @RequestParam(role = Role.POST)
    @Optional
    private final Team team;

    @RequestParam(role = Role.POST)
    @Optional
    private final Locale locale;

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
            if (attachementFileName != null && attachementDescription != null && verifyFile(attachement)) {
                file = FileMetadataManager.createFromTempFile(authenticatedMember, attachement, attachementFileName, attachementDescription);
            } else {
                if (attachementFileName == null) {
                    session.notifyError(Context.tr("Filename is empty. Could you report that bug?"));
                }
                if (attachementDescription == null) {
                    session.notifyError(Context.tr("When you add a file you have to describe it."));
                }
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

    protected final boolean propagateAttachedFileIfPossible(final UserContentInterface<?> content) {
        if (getFile() != null && content.canAddFile()) {
            try {
                content.addFile(getFile());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Yon cannot add an attachment file (Even if y tested it ...)", e);
            }
            return true;
        }
        return false;
    }

    protected final boolean propagateAsTeamIfPossible(final UserContentInterface<?> content) {
        if (getTeam() != null && content.canAccessAsTeam(getTeam())) {
            try {
                content.setAsTeam(getTeam());
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Yon cannot set AsTeam (Even if y tested it ...)", e);
            }
            return true;
        }
        return false;
    }

    protected final Team getTeam() {
        return team;
    }

    protected final Locale getLocale() {
        return locale;
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
