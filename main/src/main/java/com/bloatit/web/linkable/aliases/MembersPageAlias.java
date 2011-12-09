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
package com.bloatit.web.linkable.aliases;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.linkable.master.AliasAction;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.MembersPageAliasUrl;

@ParamContainer("m/%memberLogin%")
public final class MembersPageAlias extends AliasAction {

    @SuppressWarnings("unused")
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("You have to specify a member name."))
    private String memberLogin;

    public MembersPageAlias(final MembersPageAliasUrl url) {
        super(url, (getMember(url) == null) ? new PageNotFoundUrl() : new MemberPageUrl(getMember(url)));
    }

    private static Member getMember(final MembersPageAliasUrl url) {
        final String memberLogin = url.getMemberLogin();
        final Member m = MemberManager.getMemberByLogin(memberLogin);
        if (m != null) {
            return m;
        }
        Context.getSession().notifyError(Context.tr("Member {0} doesn''t exist.", memberLogin));
        return null;
    }
}
