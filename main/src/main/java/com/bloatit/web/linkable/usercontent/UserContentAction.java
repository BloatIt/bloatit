//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.usercontent;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.util.Locale;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
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
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.UserContentActionUrl;

@ParamContainer("usercontent/docreate")
public abstract class UserContentAction extends LoggedAction {

    @RequestParam(role = Role.POST)
    @Optional
    private Team team;

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

    private final UserContentActionUrl createUserActionurl;

    private final UserTeamRight right;

    protected UserContentAction(final UserContentActionUrl url, final Team overrideTeam, final UserTeamRight right) {
        this(url, right);
        team = overrideTeam;
    }

    protected UserContentAction(final UserContentActionUrl url, final UserTeamRight right) {
        super(url);
        this.createUserActionurl = url;
        this.right = right;
        team = url.getTeam();
        locale = url.getLocale();
        attachment = url.getAttachment();
        attachmentFileName = url.getAttachmentFileName();
        attachmentDescription = url.getAttachmentDescription();
        attachmentContentType = url.getAttachmentContentType();

        file = null;
    }

    @Override
    protected final void transmitParameters() {
        session.addParameter(createUserActionurl.getTeamParameter());
        session.addParameter(createUserActionurl.getLocaleParameter());
        doTransmitParameters();
    }

    protected abstract void doTransmitParameters();

    protected abstract boolean verifyFile(String filename);

    protected abstract Url doDoProcessRestricted(Member me, Team asTeam);

    @Override
    protected final Url doProcessRestricted(final Member me) {
        if (attachment != null) {
            if (!isEmpty(attachmentFileName) && !isEmpty(attachmentDescription) && verifyFile(attachment)) {
                file = FileMetadataManager.createFromTempFile(me, team, attachment, attachmentFileName, attachmentDescription);
            } else {
                if (isEmpty(attachmentFileName)) {
                    session.notifyError(Context.tr("Filename is empty. Could you report that bug?"));
                }
                if (isEmpty(attachmentDescription)) {
                    session.notifyError(Context.tr("When you add a file you have to describe it."));
                }
                transmitParameters();
                return doProcessErrors();
            }
        }
        try {
            if (team != null) {
                if (!team.getUserTeamRight(me).contains(right)) {
                    session.notifyBad(Context.tr("You are not allowed to do this action in the name of a team."));
                    transmitParameters();
                    return doProcessErrors();
                }
                ((AuthenticatedUserToken) Context.getSession().getUserToken()).setAsTeam(team);
            }
            return doDoProcessRestricted(me, team);
        } finally {
            ((AuthenticatedUserToken) Context.getSession().getUserToken()).setAsTeam(null);
        }
    }

    // TODO correct me.
    protected final boolean propagateAttachedFileIfPossible(final UserContentInterface content) {
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

    protected final Locale getLocale() {
        return locale;
    }
}
