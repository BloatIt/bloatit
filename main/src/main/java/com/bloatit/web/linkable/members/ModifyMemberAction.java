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
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.util.List;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.utils.StringUtils;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.LoggedElveosAction;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.ModifyMemberActionUrl;
import com.bloatit.web.url.ModifyMemberPageUrl;

@ParamContainer(value = "member/domodifymember", protocol = Protocol.HTTPS)
public class ModifyMemberAction extends LoggedElveosAction {

    @RequestParam(role = Role.POST)
    @Optional
    @MaxConstraint(max = 200, message = @tr("Number of characters for your description has to be inferior to %constraint% but was %valueLength% characters long."))
    private final String description;

    @RequestParam(role = Role.POST)
    @Optional
    @MinConstraint(min = 1, message = @tr("Number of characters for Fullname has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 30, message = @tr("Number of characters for Fullname has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String fullname;

    @RequestParam(role = Role.POST)
    @Optional
    private final Boolean deleteAvatar;

    @RequestParam(name = "avatar", role = Role.POST)
    @Optional
    private final String avatar;

    @RequestParam(name = "avatar/filename", role = Role.POST)
    @Optional
    private final String avatarFileName;

    @SuppressWarnings("unused")
    @RequestParam(name = "avatar/contenttype", role = Role.POST)
    @Optional
    private final String avatarContentType;

    private final ModifyMemberActionUrl url;

    public ModifyMemberAction(final ModifyMemberActionUrl url) {
        super(url);
        this.fullname = url.getFullname();
        this.avatar = url.getAvatar();
        this.avatarFileName = url.getAvatarFileName();
        this.avatarContentType = url.getAvatarContentType();
        this.deleteAvatar = url.getDeleteAvatar();
        this.description = url.getDescription();
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        try {
            // DESCRIPTION
            if (!StringUtils.equals(description, me.getDescription())) {
                session.notifyGood(Context.tr("Description updated."));
            }
            me.setDescription(description);

            // FULLNAME
            if (!StringUtils.equals(fullname, me.getFullname())) {
                session.notifyGood(Context.tr("Fullname updated."));
            }
            if (fullname != null) {
                me.setFullname(fullname.trim());
            } else {
                me.setFullname("");
            }

            // AVATAR
            if (avatar != null) {
                final FileConstraintChecker fcc = new FileConstraintChecker(avatar);
                final List<String> imageErr = fcc.isImageAvatar();
                if (!isEmpty(avatarFileName) && imageErr == null) {
                    final FileMetadata file = FileMetadataManager.createFromTempFile(me, null, avatar, avatarFileName, "");
                    me.setAvatar(file);
                    session.notifyGood(Context.tr("Avatar updated."));
                } else {
                    if (imageErr != null) {
                        for (final String message : imageErr) {
                            session.notifyWarning(message);
                        }
                    }
                    if (isEmpty(avatarFileName)) {
                        session.notifyError(Context.tr("Filename is empty. Could you report that bug?"));
                    }
                    transmitParameters();
                    return doProcessErrors();
                }
            }

            // DELETE AVATAR
            if (deleteAvatar != null && deleteAvatar.booleanValue()) {
                session.notifyGood(Context.tr("Avatar deleted."));
                me.setAvatar(null);
            }

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }

        return new MemberPageUrl(me);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        // Avatar and delete avatar
        if (deleteAvatar != null && deleteAvatar.booleanValue() && me.getAvatar() != null && !me.getAvatar().isNull()) {
            if (!isEmpty(avatar)) {
                url.getAvatarParameter().addErrorMessage(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
                return doProcessErrors();
            }
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyMemberPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify your account settings.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getFullnameParameter());
        session.addParameter(url.getAvatarParameter());
        session.addParameter(url.getAvatarFileNameParameter());
        session.addParameter(url.getAvatarContentTypeParameter());
        session.addParameter(url.getDescriptionParameter());
    }
}
