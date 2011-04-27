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
package com.bloatit.web.linkable.team;

import static com.bloatit.framework.utils.StringUtils.isEmpty;

import java.util.List;

import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.FileConstraintChecker;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.ModifyTeamActionUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("team/domodify")
public class ModifyTeamAction extends LoggedAction {
    @RequestParam(role = Role.GET)
    private final Team team;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("The team display name size has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "50", maxErrorMsg = @tr("The team display name size has to be inferior to %constraint% your text is %valueLength% characters long."),//
    optionalErrorMsg = @tr("You forgot to write a team name"))
    private final String displayName;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("The contact size has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "300", maxErrorMsg = @tr("The contact size has to be inferior to %constraint%."),//
    optionalErrorMsg = @tr("You forgot to write a specification"))
    private final String contact;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for description has to be superior to %constraint% but your text is %valueLength% characters long."),//
    max = "5000", maxErrorMsg = @tr("Number of characters for description has to be inferior to %constraint% but your text is %valueLength% characters long."),//
    optionalErrorMsg = @tr("You forgot to write a description"))
    private final String description;

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

    private ModifyTeamActionUrl url;

    public ModifyTeamAction(ModifyTeamActionUrl url) {
        super(url);
        this.team = url.getTeam();
        this.avatar = url.getAvatar();
        this.avatarFileName = url.getAvatarFileName();
        this.avatarContentType = url.getAvatarContentType();
        this.deleteAvatar = url.getDeleteAvatar();
        this.displayName = url.getDisplayName();
        this.contact = url.getContact();
        this.description = url.getDescription();
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        try {
            // Display name
            if (isEmpty(displayName) && !isEmpty(team.getDisplayName())) {
                session.notifyGood(Context.tr("Team's display name deleted."));
                team.setDisplayName(null);
            } else if (!isEmpty(displayName) && !displayName.equals(team.getDisplayName())) {
                session.notifyGood(Context.tr("Team's display updated."));
                team.setDisplayName(displayName);
            }

            // Contact information
            if (!isEmpty(contact.trim()) && !contact.equals(team.getContact())) {
                session.notifyGood(Context.tr("Team's contact information changed."));
                team.setContact(contact);
            }

            // Description
            if (!isEmpty(description.trim()) && !description.equals(team.getDescription())) {
                session.notifyGood(Context.tr("Team's description changed."));
                team.setDescription(description);
            }

            // AVATAR
            if (avatar != null) {
                FileConstraintChecker fcc = new FileConstraintChecker(avatar);
                List<String> imageErr = fcc.isImageAvatar();
                if (!isEmpty(avatarFileName) && imageErr == null) {
                    FileMetadata file = FileMetadataManager.createFromTempFile(me, null, avatar, avatarFileName, "");
                    team.setAvatar(file);
                    session.notifyGood(Context.tr("Avatar updated."));
                } else {
                    if (imageErr != null) {
                        for (final String message : imageErr) {
                            session.notifyBad(message);
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
                session.notifyGood(Context.tr("Deleted avatar."));
                team.setAvatar(null);
            }

        } catch (UnauthorizedOperationException e) {
            e.printStackTrace();
        }

        return new TeamPageUrl(team);
    }

    @Override
    protected Url doCheckRightsAndEverything(Member me) {
        // TODO: link error messages to form field, so they can be red
        boolean error = false;

        if (!me.hasModifyTeamRight(team)) {
            session.notifyError("You are not allowed to modify this team informations.");
            return new ModifyTeamPageUrl(team);
        }

        if (isEmpty(contact)) {
            error = true;
            session.notifyBad(Context.tr("Cannot delete team's display name."));
        }

        if (isEmpty(contact)) {
            error = true;
            session.notifyBad(Context.tr("Cannot delete team's contact information."));
        }

        if (isEmpty(description)) {
            error = true;
            session.notifyBad(Context.tr("Cannot delete team's description."));
        }

        // Avatar and delete avatar
        if (deleteAvatar != null && deleteAvatar.booleanValue() && me.getAvatar() != null && !me.getAvatar().isNull()) {
            if (!isEmpty(avatar)) {
                session.notifyBad(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
                error = true;
            }
        }

        if (error) {
            return new ModifyTeamPageUrl(team);
        }
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyTeamPageUrl(team);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to modify your account settings");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getAvatarParameter());
        session.addParameter(url.getAvatarFileNameParameter());
        session.addParameter(url.getAvatarContentTypeParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getContactParameter());
        session.addParameter(url.getDisplayNameParameter());
    }
}
