package com.bloatit.web.linkable.usercontent;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

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

    @RequestParam(name = "attachment", role = Role.POST)
    @Optional
    private final String attachment;

    @RequestParam(name = "attachment/filename", role = Role.POST)
    @Optional
    private final String attachmentFileName;

    @RequestParam(name = "attachment_description", role = Role.POST)
    @Optional
    private final String attachmentDescription;

    @RequestParam(name = "attachment/contenttype", role = Role.POST)
    @Optional
    private final String attachmentContentType;

    private FileMetadata file;

    private final CreateUserContentActionUrl createUserActionurl;

    public CreateUserContentAction(final CreateUserContentActionUrl url) {
        super(url);
        this.createUserActionurl = url;
        team = url.getTeam();
        locale = url.getLocale();
        attachment = url.getAttachment();
        attachmentFileName = url.getAttachmentFileName();
        attachmentDescription = url.getAttachmentDescription();
        attachmentContentType = url.getAttachmentContentType();

        file = null;
    }

    @Override
    protected final Url doProcessRestricted(final Member authenticatedMember) {
        if (attachment != null) {
            if (!isEmpty(attachmentFileName) && !isEmpty(attachmentDescription) && verifyFile(attachment)) {
                file = FileMetadataManager.createFromTempFile(authenticatedMember, attachment, attachmentFileName, attachmentDescription);
            } else {
                if (isEmpty(attachmentFileName)) {
                    session.notifyError(Context.tr("Filename is empty. Could you report that bug?"));
                }
                if (isEmpty(attachmentDescription)) {
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
        return attachment;
    }

    protected final String getAttachementFileName() {
        return attachmentFileName;
    }

    protected final String getAttachementDescription() {
        return attachmentDescription;
    }

    protected final String getAttachementContentType() {
        return attachmentContentType;
    }

    protected final FileMetadata getFile() {
        return file;
    }
}
