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
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
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
import com.bloatit.web.pages.master.HtmlDefineParagraph;
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
        final HtmlDiv master = new HtmlDiv("member_page");

        // Link to change account settings
        final HtmlDiv modify = new HtmlDiv("float_right");
        master.add(modify);
        modify.addText(Context.tr("Change account settings"));

        // Title
        final String title = (myPage) ? Context.tr("My page") : Context.tr("Member page");
        final HtmlTitleBlock tBlock = new HtmlTitleBlock(title, 1);
        master.add(tBlock);

        HtmlDiv main = new HtmlDiv("member");
        master.add(main);

        // Member ID card
        final HtmlDiv memberId = new HtmlDiv("member_id");

        // Avatar
        final HtmlDiv avatarDiv = new HtmlDiv("float_left");
        avatarDiv.add(MembersTools.getMemberAvatar(member));
        memberId.add(avatarDiv);
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

        final HtmlTitleBlock recent = new HtmlTitleBlock(Context.tr("Recent activity"), 2);
        main.add(recent);

        final HtmlDiv recentActivity = new HtmlDiv("recent_activity");
        recent.add(recentActivity);

        final PageIterable<UserContent<? extends DaoUserContent>> activity = member.getActivity();
        final MemberPageUrl clonedUrl = url.clone();
        HtmlPagedList<UserContent<? extends DaoUserContent>> feed;
        feed = new HtmlPagedList<UserContent<? extends DaoUserContent>>(new ActivityRenderer(), activity, clonedUrl, clonedUrl.getPagedActivityUrl());
        recentActivity.add(feed);

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
                public HtmlElement visit(final Translation model) {
                    // TODO: After implementing correct translation stuff, do
                    // something in here
                    return new HtmlParagraph("translation");
                }

                @Override
                public HtmlElement visit(final Kudos model) {
                    return new HtmlParagraph("kudos");
                }

                @Override
                public HtmlElement visit(final Contribution model) {
                    HtmlSpan contribSpan = new HtmlSpan("feed_contribution");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("<0::Contributed>"), contribSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Feature model) {
                    HtmlSpan featureSpan = new HtmlSpan("feed_feature");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Requested <0::feature>"), featureSpan);
                    return generateFeatureFeedStructure(mixedText, model, model);
                }

                @Override
                public HtmlElement visit(final Offer model) {
                    HtmlSpan offerSpan = new HtmlSpan("feed_offer");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Made an <0::offer>"), offerSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Release model) {
                    HtmlSpan releaseSpan = new HtmlSpan("feed_release");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Made a <0::release>"), releaseSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Bug model) {
                    HtmlSpan bugSpan = new HtmlSpan("feed_bug");
                    HtmlLink bugUrl = new BugPageUrl(model).getHtmlLink(model.getTitle());
                    bugUrl.setCssClass("bug_link");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Reported <0::bug> (<1::>)"), bugSpan, bugUrl);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final FileMetadata model) {
                    HtmlSpan fileSpan = new HtmlSpan("feed_file");
                    HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Uploaded a <0::file>"), fileSpan);
                    HtmlLink htmlLink = new FileResourceUrl(model).getHtmlLink(model.getFileName());
                    HtmlElement secondLine = generateFeedSecondLine(Context.tr("File: "), htmlLink);
                    return generateFeedStructure(mixedText, secondLine, model);
                    // return generateFeatureFeedStructure(mixedText,
                    // model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(Comment model) {
                    HtmlSpan commentSpan = new HtmlSpan("feed_comment");
                    HtmlMixedText mixedText;

                    if (model.getParentType() == ParentType.COMMENT) {
                        Member commenter = model.getParentComment().getMember();
                        HtmlLink commenterUrl;
                        try {
                            commenterUrl = new MemberPageUrl(commenter).getHtmlLink(commenter.getDisplayName());
                        } catch (UnauthorizedOperationException e) {
                            throw new ShallNotPassException("Cannot access member display name.", e);
                        }
                        mixedText = new HtmlMixedText(Context.tr("Replied to a <0::comment> (from <1::>)"), commentSpan, commenterUrl);

                        while (model.getParentType() == ParentType.COMMENT) {
                            model = model.getParentComment();
                        }

                    } else {
                        mixedText = new HtmlMixedText(Context.tr("<0::Commented>"), commentSpan);
                    }

                    switch (model.getParentType()) {
                        case BUG:
                            HtmlLink link = new BugPageUrl(model.getParentBug()).getHtmlLink(model.getParentBug().getTitle());
                            HtmlElement secondLine = generateFeedSecondLine(Context.tr("Bug: "), link);
                            return generateFeedStructure(mixedText, secondLine, model);
                        case FEATURE:
                            return generateFeatureFeedStructure(mixedText, model.getParentFeature(), model);
                        case COMMENT:
                        case RELEASE:
                            // Nothing to do here
                            break;
                    }
                    return new PlaceHolderElement();
                }
            });
        }
    }

    /**
     * Generates a second line of a feed
     * 
     * @param item the String to display at the start of the second line
     * @param target the element to display after <code>item</code>
     * @return the element to add as a second line
     */
    private HtmlElement generateFeedSecondLine(String item, HtmlElement target) {
        return new HtmlDefineParagraph(item, target);
    }

    /**
     * Generates a complete structure of a feed for elements that have a feature
     * on their second line
     * <p>
     * This is a convenience method for
     * {@link #generateFeedStructure(HtmlElement, HtmlElement, UserContentInterface)}
     * that avoids having to create the feature second line
     * </p>
     * 
     * @param firstLine the element to show on the first line
     * @param feature the <code>feature</code> to display on the second line
     * @param content the UserContent that originates everything, so we can get
     *            its creation date
     * @return the element to add in the feed
     */
    private HtmlElement generateFeatureFeedStructure(HtmlElement firstLine, Feature feature, UserContentInterface<? extends DaoUserContent> content) {
        PlaceHolderElement ph = new PlaceHolderElement();
        try {
            ph.add(generateFeedSecondLine(Context.tr("Feature: "), FeaturesTools.generateFeatureTitle(feature)));
        } catch (UnauthorizedOperationException e) {
            throw new ShallNotPassException("Cannot access some feature information.", e);
        }
        return generateFeedStructure(firstLine, ph, content);
    }

    /**
     * Creates a complete feed item to add to the feed
     * 
     * @param firstLine the first line of the feed item
     * @param secondLine the second line of the feed item
     * @param content the UserContent that originates everything, so we can get
     *            its creation date
     * @return the element to add in the feed
     */
    private HtmlElement generateFeedStructure(HtmlElement firstLine, HtmlElement secondLine, UserContentInterface<? extends DaoUserContent> content) {
        HtmlDiv master = new HtmlDiv("feed_item");
        master.add(new HtmlDiv("feed_item_title").add(firstLine));
        HtmlDiv secondAndThirdLine = new HtmlDiv("feed_content");
        master.add(secondAndThirdLine);
        secondAndThirdLine.add(new HtmlDiv("feed_item_description").add(secondLine));
        HtmlBranch dateBox = new HtmlDiv("feed_item_date");
        secondAndThirdLine.add(dateBox);
        String dateString = Context.tr("Date: {0}", Context.getLocalizator().getDate(content.getCreationDate()).toString(FormatStyle.LONG));
        dateBox.addText(dateString);
        return master;
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
