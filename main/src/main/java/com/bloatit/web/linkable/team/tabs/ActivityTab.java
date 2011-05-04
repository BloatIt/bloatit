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
package com.bloatit.web.linkable.team.tabs;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlRenderer;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTabBlock.HtmlTab;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
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
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.components.activity.ActivityVisitor;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer(value = "activityTab", isComponent = true)
public class ActivityTab extends HtmlTab {
    private final Team team;
    private final TeamPageUrl url;

    @SuppressWarnings("unused")
    private HtmlPagedList<UserContent<? extends DaoUserContent>> pagedActivity;

    public ActivityTab(final Team team, final String title, final String tabKey, final TeamPageUrl url) {
        super(title, tabKey);
        this.team = team;
        this.url = url;
    }

    @Override
    public XmlNode generateBody() {
        final HtmlDiv master = new HtmlDiv("tab_pane");

        // Displaying list of user recent activity
        final HtmlTitleBlock recent = new HtmlTitleBlock(Context.tr("Recent activity"), 1);
        master.add(recent);

        final HtmlDiv recentActivity = new HtmlDiv("recent_activity");
        recent.add(recentActivity);

        final PageIterable<UserContent<? extends DaoUserContent>> activity = team.getActivity();

        if(activity.size() == 0) {
            recent.add(new HtmlParagraph(Context.tr("No recent activity")));
        }

        final TeamPageUrl clonedUrl = url.clone();
        HtmlPagedList<UserContent<? extends DaoUserContent>> feed;
        feed = new HtmlPagedList<UserContent<? extends DaoUserContent>>(new ActivityRenderer(), activity, clonedUrl, clonedUrl.getActivityUrl()
                                                                                                                              .getPagedActivityUrl());
        recentActivity.add(feed);

        return master;
    }

    /**
     * Paged renderer for the activity feed
     */
    private static final class ActivityRenderer implements HtmlRenderer<UserContent<? extends DaoUserContent>> {
        public ActivityRenderer() {
            super();
        }

        @Override
        public XmlNode generate(final UserContent<? extends DaoUserContent> content) {
            return content.accept(new ActivityVisitor() {
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
                    final HtmlSpan contribSpan = new HtmlSpan("feed_contribution");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("<0::Contributed>"), contribSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Feature model) {
                    final HtmlSpan featureSpan = new HtmlSpan("feed_feature");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Requested <0::feature>"), featureSpan);
                    return generateFeatureFeedStructure(mixedText, model, model);
                }

                @Override
                public HtmlElement visit(final Offer model) {
                    final HtmlSpan offerSpan = new HtmlSpan("feed_offer");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Made an <0::offer>"), offerSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Release model) {
                    final HtmlSpan releaseSpan = new HtmlSpan("feed_release");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Made a <0::release>"), releaseSpan);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final Bug model) {
                    final HtmlSpan bugSpan = new HtmlSpan("feed_bug");
                    final HtmlLink bugUrl = new BugPageUrl(model).getHtmlLink(model.getTitle());
                    bugUrl.setCssClass("bug_link");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Reported <0::bug> (<1::>)"), bugSpan, bugUrl);
                    return generateFeatureFeedStructure(mixedText, model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(final FileMetadata model) {
                    final HtmlSpan fileSpan = new HtmlSpan("feed_file");
                    final HtmlMixedText mixedText = new HtmlMixedText(Context.tr("Uploaded a <0::file>"), fileSpan);
                    final HtmlLink htmlLink = new FileResourceUrl(model).getHtmlLink(model.getFileName());
                    final HtmlElement secondLine = generateFeedSecondLine(Context.tr("File: "), htmlLink);
                    return generateFeedStructure(mixedText, secondLine, model);
                    // return generateFeatureFeedStructure(mixedText,
                    // model.getFeature(), model);
                }

                @Override
                public HtmlElement visit(Comment model) {
                    final HtmlSpan commentSpan = new HtmlSpan("feed_comment");
                    HtmlMixedText mixedText;

                    if (model.getParentType() == ParentType.COMMENT) {
                        final Member commenter = model.getParentComment().getMember();
                        HtmlLink commenterUrl;
                        commenterUrl = new MemberPageUrl(commenter).getHtmlLink(commenter.getDisplayName());
                        mixedText = new HtmlMixedText(Context.tr("Replied to a <0::comment> (from <1::>)"), commentSpan, commenterUrl);

                        while (model.getParentType() == ParentType.COMMENT) {
                            model = model.getParentComment();
                        }

                    } else {
                        mixedText = new HtmlMixedText(Context.tr("<0::Commented>"), commentSpan);
                    }

                    switch (model.getParentType()) {
                        case BUG:
                            final HtmlLink link = new BugPageUrl(model.getParentBug()).getHtmlLink(model.getParentBug().getTitle());
                            final HtmlElement secondLine = generateFeedSecondLine(Context.tr("Bug: "), link);
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

        /**
         * Generates a second line of a feed
         *
         * @param item the String to display at the start of the second line
         * @param target the element to display after <code>item</code>
         * @return the element to add as a second line
         */
        private HtmlElement generateFeedSecondLine(final String item, final HtmlElement target) {
            return new HtmlDefineParagraph(item, target);
        }

        /**
         * Generates a complete structure of a feed for elements that have a
         * feature on their second line
         * <p>
         * This is a convenience method for
         * {@link #generateFeedStructure(HtmlElement, HtmlElement, UserContentInterface)}
         * that avoids having to create the feature second line
         * </p>
         *
         * @param firstLine the element to show on the first line
         * @param feature the <code>feature</code> to display on the second line
         * @param content the UserContent that originates everything, so we can
         *            get its creation date
         * @return the element to add in the feed
         */
        private HtmlElement generateFeatureFeedStructure(final HtmlElement firstLine, final Feature feature, final UserContentInterface content) {
            final PlaceHolderElement ph = new PlaceHolderElement();
            try {
                ph.add(generateFeedSecondLine(Context.tr("Feature: "), FeaturesTools.generateFeatureTitle(feature)));
            } catch (final UnauthorizedOperationException e) {
                throw new ShallNotPassException("Cannot access some feature information.", e);
            }
            return generateFeedStructure(firstLine, ph, content);
        }

        /**
         * Creates a complete feed item to add to the feed
         *
         * @param firstLine the first line of the feed item
         * @param secondLine the second line of the feed item
         * @param content the UserContent that originates everything, so we can
         *            get its creation date
         * @return the element to add in the feed
         */
        private HtmlElement generateFeedStructure(final HtmlElement firstLine, final HtmlElement secondLine, final UserContentInterface content) {
            final HtmlDiv master = new HtmlDiv("feed_item");
            // Title
            master.add(new HtmlDiv("feed_item_title").add(firstLine));
            final HtmlDiv activityDetail = new HtmlDiv("feed_content");
            master.add(activityDetail);
            activityDetail.add(new HtmlDiv("feed_item_description").add(secondLine));

            // Author
            final HtmlBranch authorBox = new HtmlDiv("feed_item_author");
            activityDetail.add(authorBox);
            final MemberPageUrl to = new MemberPageUrl(content.getMember());
            authorBox.add(new HtmlDefineParagraph(Context.tr("Author: "), to.getHtmlLink(content.getMember().getDisplayName())));

            // Date
            final HtmlBranch dateBox = new HtmlDiv("feed_item_date");
            activityDetail.add(dateBox);
            final String dateString = Context.tr("Date: {0}", Context.getLocalizator().getDate(content.getCreationDate()).toString(FormatStyle.LONG));
            dateBox.addText(dateString);

            return master;
        }
    }
}
