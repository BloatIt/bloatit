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

import com.bloatit.data.DaoTeam.Right;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.utils.FileConstraintChecker;
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
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.ModifyTeamActionUrl;
import com.bloatit.web.url.ModifyTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("teams/%team%/domodify")
public class ModifyTeamAction extends LoggedElveosAction {
    @RequestParam(role = Role.PAGENAME)
    private final Team team;

    @RequestParam(role = Role.POST)
    @MinConstraint(min = 4, message = @tr("The team display name size has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 50, message = @tr("The team display name size has to be inferior to %constraint% your text is %valueLength% characters long."))
    @NonOptional(@tr("You forgot to write a team name"))
    @FormField(label = @tr("Team name"))
    private final String displayName;

    @RequestParam(role = Role.POST)
    @MinConstraint(min = 4, message = @tr("The contact size has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 300, message = @tr("The contact size has to be inferior to %constraint%."))
    @NonOptional(@tr("You forgot to write a specification"))
    @FormField(label = @tr("Contact information"))
    private final String contact;

    @RequestParam(role = Role.POST)
    @MinConstraint(min = 4, message = @tr("Number of characters for description has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 5000, message = @tr("Number of characters for description has to be inferior to %constraint% but your text is %valueLength% characters long."))
    @NonOptional(@tr("You forgot to write a description"))
    @FormField(label = @tr("Team description"))
    private final String description;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("Delete avatar"))
    @FormComment(@tr("Checking this box will delete team's avatar."))
    private final Boolean deleteAvatar;

    @RequestParam(name = "avatar", role = Role.POST)
    @Optional
    @FormField(label = @tr("Avatar of your team"))
    @FormComment(@tr("64px x 64px. 50Kb max. Accepted formats: png, jpg"))
    private final String avatar;

    @RequestParam(name = "avatar/filename", role = Role.POST)
    @Optional
    private final String avatarFileName;

    @SuppressWarnings("unused")
    @RequestParam(name = "avatar/contenttype", role = Role.POST)
    @Optional
    private final String avatarContentType;

    @RequestParam(role = Role.POST)
    @Optional
    @FormComment(@tr("\"Open to all\" teams can be joined by anybody without an invitation."))
    @FormField(label = @tr("Team membership"))
    private final Right right;

    private final ModifyTeamActionUrl url;

    public ModifyTeamAction(final ModifyTeamActionUrl url) {
        super(url);
        this.team = url.getTeam();
        this.avatar = url.getAvatar();
        this.avatarFileName = url.getAvatarFileName();
        this.avatarContentType = url.getAvatarContentType();
        this.deleteAvatar = url.getDeleteAvatar();
        this.displayName = url.getDisplayName();
        this.contact = url.getContact();
        this.description = url.getDescription();
        this.right = url.getRight();
        this.url = url;
    }

    @Override
    protected Url doProcessRestricted(final Member me) {
        try {
            // Display name
            if (!isEmpty(displayName) && !displayName.equals(team.getLogin())) {
                session.notifyGood(Context.tr("Team's display updated."));
                team.setLogin(displayName);
            }

            // Contact information
            if (!isEmpty(contact.trim()) && !contact.equals(team.getPublicContact())) {
                session.notifyGood(Context.tr("Team's contact information changed."));
                team.setPublicContact(contact);
            }

            // Description
            if (!isEmpty(description.trim()) && !description.equals(team.getDescription())) {
                session.notifyGood(Context.tr("Team's description changed."));
                team.setDescription(description, me);
            }

            // AVATAR
            if (avatar != null) {
                final FileConstraintChecker fcc = new FileConstraintChecker(avatar);
                final List<String> imageErr = fcc.isImageAvatar();
                if (!isEmpty(avatarFileName) && imageErr == null) {
                    final FileMetadata file = FileMetadataManager.createFromTempFile(me, null, avatar, avatarFileName, "");
                    team.setAvatar(file);
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

            // Description
            if (right != null && !right.equals(team.getJoinRight())) {
                session.notifyGood(Context.tr("Team's join right changed."));
                team.setRight(right);
            }

            // DELETE AVATAR
            if (deleteAvatar != null && deleteAvatar.booleanValue()) {
                session.notifyGood(Context.tr("Deleted avatar."));
                team.setAvatar(null);
            }

        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException(e);
        }

        return new TeamPageUrl(team);
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        boolean error = false;

        if (!me.hasModifyTeamRight(team)) {
            session.notifyError("You are not allowed to modify this team informations.");
            return new ModifyTeamPageUrl(team);
        }

        if (!isEmpty(displayName.trim()) && !displayName.equals(team.getLogin()) && TeamManager.exist(displayName)) {
            error = true;
            session.notifyError(Context.tr("This team name already exists."));
            url.getDisplayNameParameter().addErrorMessage(Context.tr("This team name already exists."));
        }
        if (isEmpty(contact.trim())) {
            error = true;
            session.notifyError(Context.tr("Cannot delete team's display name."));
            url.getContactParameter().addErrorMessage(Context.tr("Cannot delete team's display name."));
        }

        if (isEmpty(description.trim())) {
            error = true;
            session.notifyError(Context.tr("Cannot delete team's description."));
            url.getDescriptionParameter().addErrorMessage(Context.tr("Cannot delete team's description."));
        }

        // Avatar and delete avatar
        if (deleteAvatar != null && deleteAvatar.booleanValue() && me.getAvatar() != null && !me.getAvatar().isNull()) {
            if (!isEmpty(avatar)) {
                session.notifyError(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
                url.getAvatarParameter().addErrorMessage(Context.tr("You cannot delete your avatar, and indicate a new one at the same time."));
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
        return Context.tr("You must be logged to modify your account settings.");
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
