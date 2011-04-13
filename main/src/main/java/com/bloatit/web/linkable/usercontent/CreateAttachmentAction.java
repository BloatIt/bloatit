package com.bloatit.web.linkable.usercontent;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

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
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CreateAttachmentActionUrl;

@ParamContainer("attachment/docreate")
public abstract class CreateAttachmentAction extends LoggedAction {
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

    public CreateAttachmentAction(final CreateAttachmentActionUrl url) {
        super(url);
        attachment = url.getAttachment();
        attachmentFileName = url.getAttachmentFileName();
        attachmentDescription = url.getAttachmentDescription();
        attachmentContentType = url.getAttachmentContentType();

        file = null;
    }

    protected abstract boolean verifyFile(String filename);

    protected abstract Url doDoProcessRestricted(Member authenticatedMember);

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

    // TODO correct me.
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
