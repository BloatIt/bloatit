/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.members;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Member;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.ChangeAvatarActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("member/changeavatar")
public final class ChangeAvatarAction extends LoggedAction {

    public static final String MEMBER = "member";
    public static final String AVATAR_CODE = "avatar";
    public static final String AVATAR_NAME_CODE = "avatar/filename";
    public static final String AVATAR_CONTENT_TYPE_CODE = "avatar/contenttype";

    @ParamConstraint(optionalErrorMsg = @tr("An avatar must be linked to a member"))
    @RequestParam(name = MEMBER)
    private final Member member;

    @ParamConstraint(optionalErrorMsg = @tr("You must provide an image as avatar"))
    @RequestParam(name = AVATAR_CODE, role = Role.POST)
    private final String avatar;

    @ParamConstraint
    @RequestParam(name = AVATAR_NAME_CODE, role = Role.POST)
    private final String avatarFileName;

    @SuppressWarnings("unused")
    @ParamConstraint
    @RequestParam(name = AVATAR_CONTENT_TYPE_CODE, role = Role.POST)
    private final String avatarContentType;
    private final ChangeAvatarActionUrl url;

    public ChangeAvatarAction(final ChangeAvatarActionUrl url) {
        super(url);
        this.url = url;

        this.member = url.getMember();
        this.avatar = url.getAvatar();
        this.avatarFileName = url.getAvatarFileName();
        this.avatarContentType = url.getAvatarContentType();
    }

    @Override
    public Url doProcessRestricted(final Member authenticatedMember) {
        // TODO create a member.canAccessAvatar.
        final FileMetadata avatarfm = FileMetadataManager.createFromTempFile(member, avatar, avatarFileName, "avatar image");

        member.setAvatar(avatarfm);

        session.notifyGood(tr("Avatar change to ''{0}''", avatarFileName));
        return Context.getSession().pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() {
        if (member != null) {
            return Context.getSession().getLastVisitedPage();
        }
        return Context.getSession().getLastVisitedPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You have to be logged to change your avatar");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getMemberParameter());
    }

}
