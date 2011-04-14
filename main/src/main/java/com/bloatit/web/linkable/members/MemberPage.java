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

import java.util.Locale;

import com.bloatit.data.DaoUserContent;
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
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.AbstractModelClassVisitor;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Comment.ParentType;
import com.bloatit.model.Contribution;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.Team;
import com.bloatit.model.Translation;
import com.bloatit.model.UserContent;
import com.bloatit.model.UserContentInterface;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.SideBarButton;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ChangeAvatarActionUrl;
import com.bloatit.web.url.FileResourceUrl;
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
    private final MemberPageUrl url;

    @SuppressWarnings("unused")
    private HtmlPagedList<UserContent<? extends DaoUserContent>> pagedActivity;

    @ParamConstraint(optionalErrorMsg = @tr("The id of the member is incorrect or missing"))
    @RequestParam(name = "id")
    private final Member member;

    private boolean myPage;

    public MemberPage(final MemberPageUrl url) {
        super(url);
        this.url = url;
        this.member = url.getMember();
        if (!session.isLogged() || !member.equals(session.getAuthToken().getMember())) {
            this.myPage = false;
        } else {
            this.myPage = true;
        }
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMemberPageMain());

        // Buttons private message & invite in team
        if (myPage) {
            layout.addRight(new SideBarButton(Context.tr("View my private messages"), WebConfiguration.getImgMessage()));
            layout.addRight(new SideBarButton(Context.tr("View my team invitations"), WebConfiguration.getImgTeam()));
        } else {
            layout.addRight(new SideBarButton(Context.tr("Send a private message"), WebConfiguration.getImgMessage()));
            layout.addRight(new SideBarButton(Context.tr("Invite to join a team"), WebConfiguration.getImgTeam()));
        }

        // Adding list of teams
        final TitleSideBarElementLayout teamBlock = new TitleSideBarElementLayout();
        try {
            if (myPage) {
                teamBlock.setTitle(Context.tr("My teams"));
            } else {
                teamBlock.setTitle(Context.tr("{0} teams", member.getDisplayName()));
            }

            final HtmlList teamList = new HtmlList();
            teamList.setCssClass("member_teams_list");
            teamBlock.add(teamList);

            for (final Team team : member.getTeams()) {
                teamList.add(new TeamPageUrl(team).getHtmlLink(team.getLogin()));
            }
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Cannot access member team information", e);
        }
        layout.addRight(teamBlock);

        return layout;
    }

    private HtmlElement generateMemberPageMain() {
        final HtmlDiv master = new HtmlDiv("member");

        // Link to change account settings
        final HtmlDiv modify = new HtmlDiv("float_right");
        master.add(modify);
        modify.addText(Context.tr("Change account settings"));

        // Title
        final String title = (myPage) ? Context.tr("My page") : Context.tr("Member page");
        final HtmlTitleBlock main = new HtmlTitleBlock(title, 1);
        master.add(main);

        // Avatar
        final HtmlDiv avatarDiv = new HtmlDiv("float_left");
        avatarDiv.add(MembersTools.getMemberAvatar(member));
        main.add(avatarDiv);

        // Member ID card
        final HtmlDiv memberId = new HtmlDiv("member_id");
        main.add(memberId);
        try {

            final HtmlList memberIdList = new HtmlList();
            memberId.add(memberIdList);

            if (myPage) {
                // Login
                final HtmlSpan login = new HtmlSpan("id_category");
                login.addText(Context.trc("login (noun)", "Login: "));
                memberIdList.add(new PlaceHolderElement().add(login).addText(member.getLogin()));

                // Fullname
                final HtmlSpan fullname = new HtmlSpan("id_category");
                fullname.addText(Context.tr("Fullname: "));
                memberIdList.add(new PlaceHolderElement().add(fullname).addText(member.getFullname()));

                // Email
                final HtmlSpan email = new HtmlSpan("id_category");
                email.addText(Context.tr("Email: "));
                memberIdList.add(new PlaceHolderElement().add(email).addText(member.getEmail()));

            } else {
                final HtmlSpan name = new HtmlSpan("id_category");
                name.addText(Context.tr("Name: "));
                memberIdList.add(new PlaceHolderElement().add(name).addText(member.getDisplayName()));
            }

            final Locale userLocale = Context.getLocalizator().getLocale();
            // Country
            final HtmlSpan country = new HtmlSpan("id_category");
            country.addText(Context.tr("Country: "));
            memberIdList.add(new PlaceHolderElement().add(country).addText(member.getLocale().getDisplayCountry(userLocale)));

            // Language
            final HtmlSpan language = new HtmlSpan("id_category");
            language.addText(Context.tr("Language: "));
            memberIdList.add(new PlaceHolderElement().add(language).addText(member.getLocale().getDisplayLanguage(userLocale)));

            // Karma
            final HtmlSpan karma = new HtmlSpan("id_category");
            karma.addText(Context.tr("Karma: "));
            memberIdList.add(new PlaceHolderElement().add(karma).addText("" + member.getKarma()));
        } catch (final UnauthorizedOperationException e) {
            session.notifyError("An error prevented us from displaying user information. Please notify us.");
            throw new ShallNotPassException("Error while gathering user information", e);
        }

        main.add(new HtmlClearer());

        // Displaying list of user recent activity
        final HtmlDiv recentActivity = new HtmlDiv("recent_activity");
        main.add(recentActivity);

        final HtmlTitleBlock recent = new HtmlTitleBlock(Context.tr("Recent activity"), 2);
        recentActivity.add(recent);

        final PageIterable<UserContent<? extends DaoUserContent>> activity = member.getActivity();
        final MemberPageUrl clonedUrl = url.clone();
        HtmlPagedList<UserContent<? extends DaoUserContent>> feed;
        feed = new HtmlPagedList<UserContent<? extends DaoUserContent>>(new ActivityRenderer(), activity, clonedUrl, clonedUrl.getPagedActivityUrl());
        recent.add(feed);

        return master;
    }

    /**
     * Paged renderer for the activity feed
     */
    private final class ActivityRenderer implements HtmlRenderer<UserContent<? extends DaoUserContent>> {
        public ActivityRenderer() {
            super();
        }

        @Override
        public XmlNode generate(final UserContent<? extends DaoUserContent> content) {
            return content.accept(new AbstractModelClassVisitor<HtmlElement>() {
                @Override
                public HtmlElement visit(final Contribution model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final String contentType = generateDate(model) + Context.tr("Contributed on the feature request: ");
                    feedBox.addText(contentType);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(generateFeature(model.getFeature()));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(Comment model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    String contentType = "";
                    if (model.getParentType() == ParentType.COMMENT) {
                        try {
                            final MemberPageUrl memberPageUrl = new MemberPageUrl(model.getParentComment().getMember());
                            final HtmlLink htmlLink = memberPageUrl.getHtmlLink(model.getParentComment().getMember().getDisplayName());
                            final String tr = Context.tr("[{0} Replied to comment from <0:comment author:> on: ", generateDate(model));
                            final HtmlMixedText mixedText = new HtmlMixedText(tr, htmlLink);
                            feedBox.add(mixedText);
                        } catch (final UnauthorizedOperationException e) {
                            throw new ShallNotPassException("Error while generating activity feed", e);
                        }
                        Comment c = model;
                        while (c.getParentType() == ParentType.COMMENT) {
                            c = c.getParentComment();
                        }
                        model = c;
                    } else {
                        contentType = generateDate(model) + Context.tr("Commented on : ");
                        feedBox.addText(contentType);
                    }

                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);

                    switch (model.getParentType()) {
                        case BUG:
                            final HtmlLink htmlLink = new BugPageUrl(model.getParentBug()).getHtmlLink();
                            final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("A <0:bug title:bug> in feature: "), htmlLink);
                            targetBox.add(mixedText);
                            final XmlNode f = generateFeature(model.getParentBug().getFeature());
                            targetBox.add(f);
                        case COMMENT:
                            // Shouldn't happen
                            break;
                        case FEATURE:
                            targetBox.add(generateFeature(model.getParentFeature()));
                            break;
                        case RELEASE:
                            break;
                    }
                    return feedBox;
                }

                private HtmlBranch generateFeature(final Feature feature) {
                    try {
                        return FeaturesTools.generateFeatureTitle(feature);
                    } catch (final UnauthorizedOperationException e) {
                        throw new ShallNotPassException("Error when generating feature box", e);
                    }
                }

                @Override
                public HtmlElement visit(final Feature model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final String contentType = generateDate(model) + Context.tr("Made a feature request: ");
                    feedBox.addText(contentType);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(generateFeature(model));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(final Offer model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final String contentType = generateDate(model) + Context.tr("Made an offer on the feature request: ");
                    feedBox.addText(contentType);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(generateFeature(model.getFeature()));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(final Release model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final String contentType = generateDate(model) + Context.tr("Made a new release: ");
                    feedBox.addText(contentType);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(generateFeature(model.getFeature()));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(final Bug model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final HtmlLink htmlLink = new BugPageUrl(model).getHtmlLink(model.getTitle());
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Reported bug (<0:bug title:>) on feature: "), htmlLink);
                    final String contentType = generateDate(model);
                    feedBox.addText(contentType);
                    feedBox.add(mixedText);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(generateFeature(model.getFeature()));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(final FileMetadata model) {
                    final HtmlDiv feedBox = new HtmlDiv("feed_box");
                    final String contentType = generateDate(model) + Context.tr("Uploaded a file: ");
                    feedBox.addText(contentType);
                    final HtmlDiv targetBox = new HtmlDiv("feed_target");
                    feedBox.add(targetBox);
                    targetBox.add(new FileResourceUrl(model).getHtmlLink(model.getFileName()));
                    return feedBox;
                }

                @Override
                public HtmlElement visit(final Kudos model) {
                    return new PlaceHolderElement();
                }

                @Override
                public HtmlElement visit(final Translation model) {
                    // TODO: After implementing correct translation stuff, do
                    // something in here
                    return new PlaceHolderElement();
                }
            });
        }
    }

    private String generateDate(final UserContentInterface<? extends DaoUserContent> content) {
        return "[" + Context.getLocalizator().getDate(content.getCreationDate()).toString() + "] ";
    }

    private XmlNode generateAvatarChangeForm() {
        final ChangeAvatarActionUrl changeAvatarActionUrl = new ChangeAvatarActionUrl(member);
        final HtmlForm changeAvatarForm = new HtmlForm(changeAvatarActionUrl.urlString());
        changeAvatarForm.enableFileUpload();

        // File
        final FieldData avatarField = changeAvatarActionUrl.getAttachmentParameter().pickFieldData();
        final HtmlFileInput avatarInput = new HtmlFileInput(avatarField.getName(), Context.tr("Avatar image file"));
        avatarInput.setComment(tr("64px x 64px. 50Kb max. Accepted formats: png, jpg"));
        changeAvatarForm.add(avatarInput);

        final HtmlSubmit submit = new HtmlSubmit(Context.tr("Submit"));
        changeAvatarForm.add(submit);

        return changeAvatarForm;
    }

    @Override
    protected String createPageTitle() {
        try {
            return tr("Member - ") + member.getLogin();
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from displaying user information. Please notify us."));
            throw new ShallNotPassException("User cannot access user information", e);
        }
    }

    @Override
    public boolean isStable() {
        return true;
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
