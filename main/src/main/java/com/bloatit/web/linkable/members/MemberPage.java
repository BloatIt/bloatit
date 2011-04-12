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

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlListItem;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.Action;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ChangeAvatarActionUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * A page used to display member information.
 * </p>
 * <p>
 * If the consulted member is the same as the logged member, then this page will
 * propose to edit account parameters
 * </p>
 */
@ParamContainer("member")
public final class MemberPage extends MasterPage {
    // Keep me here ! I am needed for the Url generation !
    private HtmlPagedList<Team> pagedTeamList;
    private final MemberPageUrl url;

    public static final String MEMBER_FIELD_NAME = "id";

    @ParamConstraint(optionalErrorMsg = @tr("The id of the member is incorrect or missing"))
    @RequestParam(name = MEMBER_FIELD_NAME)
    private final Member member;

    public MemberPage(final MemberPageUrl url) {
        super(url);
        this.url = url;
        this.member = url.getMember();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMemberPageMain());

        return layout;
    }

    private HtmlElement generateMemberPageMain() {
        try {
            final HtmlDiv master = new HtmlDiv();

            final HtmlTitleBlock memberTitle = new HtmlTitleBlock(Context.tr("Member: ") + member.getDisplayName(), 1);
            master.add(memberTitle);
            // Display the avatar at the right size of the block
            memberTitle.add(new HtmlDiv("float_right").add(MembersTools.getMemberAvatar(member)));
            final HtmlList memberInfo = new HtmlList();
            memberTitle.add(memberInfo);

            memberInfo.add((tr("Full name: ") + member.getFullname()));
            memberInfo.add(Context.trc("Login (noun) ", "Login: ") + member.getLogin());
            if (member.canAccessEmail(Action.READ)) {
                memberInfo.add(new HtmlText(tr("Email: ") + member.getEmail()));
            }
            memberInfo.add(new HtmlText(tr("Karma: ") + member.getKarma()));
            if (member.canAccessLocale(Action.READ)) {
                memberInfo.add(new HtmlText(tr("Country: ") + member.getLocale().getDisplayCountry(Context.getLocalizator().getLocale())));
                memberInfo.add(new HtmlText(tr("Language: ") + member.getLocale().getDisplayLanguage(Context.getLocalizator().getLocale())));
            }

            // A list of all users team
            final HtmlTitleBlock memberTeams = new HtmlTitleBlock(Context.tr("List of teams"), 2);
            memberTitle.add(memberTeams);
            final PageIterable<Team> teamList = member.getTeams();
            if (teamList.size() > 0) {
                @SuppressWarnings("synthetic-access") final HtmlRenderer<Team> teamRenderer = new TeamListRenderer();
                final MemberPageUrl clonedUrl = new MemberPageUrl(url);
                pagedTeamList = new HtmlPagedList<Team>(teamRenderer, teamList, clonedUrl, clonedUrl.getPagedTeamListUrl());
                memberTeams.add(pagedTeamList);
            } else {
                memberTeams.addText(Context.tr("Not part of any team now"));
            }

            // Change avatar (only is the member is the user)
            if (member.isOwner()) {
                final HtmlTitleBlock changeAvatar = new HtmlTitleBlock(Context.tr("Change avatar"), 2);
                changeAvatar.add(generateAvatarChangeForm());
                master.add(changeAvatar);
            }
            return master;
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }
    }

    private XmlNode generateAvatarChangeForm() {
        final ChangeAvatarActionUrl changeAvatarActionUrl = new ChangeAvatarActionUrl(member);
        final HtmlForm changeAvatarForm = new HtmlForm(changeAvatarActionUrl.urlString());
        changeAvatarForm.enableFileUpload();

        // File
        final HtmlFileInput avatarInput = new HtmlFileInput(ChangeAvatarAction.AVATAR_CODE, Context.tr("Avatar image file"));
        avatarInput.setComment(tr("64px x 64px. 50Kb max. Accepted formats: png, jpg"));
        changeAvatarForm.add(avatarInput);

        final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));
        changeAvatarForm.add(submit);

        return changeAvatarForm;
    }

    @Override
    protected String createPageTitle() {
        if (member != null) {
            try {
                return tr("Member - ") + member.getLogin();
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
                throw new ShallNotPassException("User cannot access user information", e);
            }
        }
        return tr("Member - No member");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private class TeamListRenderer implements HtmlRenderer<Team> {
        @Override
        public XmlNode generate(final Team team) {
            final TeamPageUrl teamUrl = new TeamPageUrl(team);
            try {
                HtmlLink htmlLink;
                htmlLink = teamUrl.getHtmlLink(team.getLogin());

                return new HtmlListItem(htmlLink);
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn(e);
            }
            return new PlaceHolderElement();
        }
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return MemberPage.generateBreadcrumb(member);
    }

    public static Breadcrumb generateBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = MembersListPage.generateBreadcrumb();

        try {
            breadcrumb.pushLink(new MemberPageUrl(member).getHtmlLink(member.getDisplayName()));
        } catch (final UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }

        return breadcrumb;
    }
}
