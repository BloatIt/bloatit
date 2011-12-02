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
package com.bloatit.web.linkable.timeline;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bloatit.data.DaoMember.EmailStrategy;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.mail.EventFeatureComponent;
import com.bloatit.mail.HtmlEntry;
import com.bloatit.mail.TimelineEventVisitor;
import com.bloatit.mail.TimelineEventVisitor.BugEntries;
import com.bloatit.mail.TimelineEventVisitor.DayAgreggator;
import com.bloatit.mail.TimelineEventVisitor.Entries;
import com.bloatit.mail.TimelineEventVisitor.FeatureEntries;
import com.bloatit.model.Image;
import com.bloatit.model.Member;
import com.bloatit.model.managers.EventManager;
import com.bloatit.model.managers.EventManager.EventList;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.url.CreateFeatureProcessUrl;
import com.bloatit.web.url.ManageFollowPageUrl;
import com.bloatit.web.url.ReadTimelineActionUrl;
import com.bloatit.web.url.TimelinePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("timeline")
public final class TimelinePage extends LoggedElveosPage {

    private static final int MIN_LEFT_RIGHT_DIFF = 50;
    private static final int MIN_DAY_HEIGHT = 70;
    private final TimelinePageUrl url;
    private Date lastWatchedEvents;

    public TimelinePage(final TimelinePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return tr("Timeline");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {

        final HtmlDiv layout = new HtmlDiv("timeline_page");

        lastWatchedEvents = loggedUser.getLastWatchedEvents();
        
        final HtmlDiv menuBar = new HtmlDiv("menu_bar");
        layout.add(menuBar);
        {
            final HtmlDiv menuBarItemNewFeature = new HtmlDiv("menu_bar_item");
            menuBar.add(menuBarItemNewFeature);
            {
                final HtmlDiv menuBarItemImage = new HtmlDiv("menu_bar_item_image");
                menuBarItemNewFeature.add(menuBarItemImage);
                menuBarItemImage.add(new HtmlImage(new Image(WebConfiguration.getImgIdeaSmall()), Context.tr("Request a feature")));
                final HtmlDiv menuBarItemLink = new HtmlDiv("menu_bar_item_link");
                menuBarItemNewFeature.add(menuBarItemLink);
                menuBarItemLink.add(new CreateFeatureProcessUrl().getHtmlLink(Context.tr("Request a feature")));
            }

            final HtmlDiv menuBarItemManageFollow = new HtmlDiv("menu_bar_item");
            menuBar.add(menuBarItemManageFollow);
            {
                final HtmlDiv menuBarItemImage = new HtmlDiv("menu_bar_item_image");
                menuBarItemManageFollow.add(menuBarItemImage);
                menuBarItemImage.add(new HtmlImage(new Image(WebConfiguration.getImgManageSmall()), Context.tr("Manage follows")));
                final HtmlDiv menuBarItemLink = new HtmlDiv("menu_bar_item_link");
                menuBarItemManageFollow.add(menuBarItemLink);
                menuBarItemLink.add(new ManageFollowPageUrl().getHtmlLink(Context.tr("Manage follows")));
            }

            final HtmlDiv menuBarItemManageNotif = new HtmlDiv("menu_bar_item");
            menuBar.add(menuBarItemManageNotif);
            {
                final HtmlDiv menuBarItemImage = new HtmlDiv("menu_bar_item_image");
                menuBarItemManageNotif.add(menuBarItemImage);
                menuBarItemImage.add(new HtmlImage(new Image(WebConfiguration.getImgManageNotifSmall()), Context.tr("Manage notifications")));
                final HtmlDiv menuBarItemLink = new HtmlDiv("menu_bar_item_link");
                menuBarItemManageNotif.add(menuBarItemLink);
                menuBarItemLink.add(new PageNotFoundUrl().getHtmlLink(Context.tr("Manage notifications")));
            }

            final HtmlDiv menuBarItemRSS = new HtmlDiv("menu_bar_item");
            menuBar.add(menuBarItemRSS);
            {
                final HtmlDiv menuBarItemImage = new HtmlDiv("menu_bar_item_image");
                menuBarItemRSS.add(menuBarItemImage);
                menuBarItemImage.add(new HtmlImage(new Image(WebConfiguration.getImgRssSmall()), Context.tr("Rss feed")));
                final HtmlDiv menuBarItemLink = new HtmlDiv("menu_bar_item_link");
                menuBarItemRSS.add(menuBarItemLink);
                menuBarItemLink.add(new PageNotFoundUrl().getHtmlLink(Context.tr("Rss feed")));
            }
            
            final HtmlDiv menuBarItemSetAsRead = new HtmlDiv("menu_bar_right_item");
            menuBar.add(menuBarItemSetAsRead);
            {
                menuBarItemSetAsRead.add(new ReadTimelineActionUrl(Context.getSession().getShortKey()).getHtmlLink(Context.tr("set as read")));
            }
        }

        final HtmlDiv timelineBlock = new HtmlDiv("timeline_block");
        layout.add(timelineBlock);
        {
            final HtmlDiv leftColumn = new HtmlDiv("left_column");
            timelineBlock.add(leftColumn);
            final HtmlDiv timeColumn = new HtmlDiv("time_column");

            PlaceHolderElement daysPlaceHolder = new PlaceHolderElement();

            timelineBlock.add(timeColumn);
            {
                final HtmlDiv timeColumnHeader = new HtmlDiv("time_column_header");
                timeColumn.add(timeColumnHeader);

                timeColumn.add(daysPlaceHolder);

                final HtmlDiv timeColumnFooter = new HtmlDiv("time_column_footer");
                timeColumn.add(timeColumnFooter);
            }

            final HtmlDiv rightColumn = new HtmlDiv("right_column");
            timelineBlock.add(rightColumn);

            EventList events = EventManager.getAllEventByMemberAfter(DateUtils.dawnOfTime(), loggedUser);

            final TimelineEventVisitor visitor = new TimelineEventVisitor(getLocalizator());

            while (events.hasNext()) {
                events.next();
                //if (events.member() != null && events.member().equals(loggedUser)) {
                    events.event().getEvent().accept(visitor);

                //}

            }

            fillTimeLine(leftColumn, daysPlaceHolder, rightColumn, visitor, false);
            
           
        }
        
        final HtmlDiv timelineBlockOnColumn = new HtmlDiv("timeline_block_one_column");
        layout.add(timelineBlockOnColumn);
        {
            final HtmlDiv timeColumn = new HtmlDiv("time_column");

            PlaceHolderElement daysPlaceHolder = new PlaceHolderElement();

            timelineBlockOnColumn.add(timeColumn);
            {
                final HtmlDiv timeColumnHeader = new HtmlDiv("time_column_header");
                timeColumn.add(timeColumnHeader);

                timeColumn.add(daysPlaceHolder);

                final HtmlDiv timeColumnFooter = new HtmlDiv("time_column_footer");
                timeColumn.add(timeColumnFooter);
            }

            final HtmlDiv rightColumn = new HtmlDiv("right_column");
            timelineBlockOnColumn.add(rightColumn);

            EventList events = EventManager.getAllEventByMemberAfter(DateUtils.dawnOfTime(), loggedUser);

            final TimelineEventVisitor visitor = new TimelineEventVisitor(getLocalizator());

            while (events.hasNext()) {
                events.next();
                //if (events.member() != null && events.member().equals(loggedUser)) {
                    events.event().getEvent().accept(visitor);

                //}

            }

            fillTimeLine(null, daysPlaceHolder, rightColumn, visitor, true);
            
            
            
            
        }
        
        return layout;
    }

    private void fillTimeLine(final HtmlDiv leftColumn,
                              PlaceHolderElement daysPlaceHolder,
                              final HtmlDiv rightColumn,
                              final TimelineEventVisitor visitor, final boolean rightOnly) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("MMM d", Context.getLocalizator().getLocale());
        //boolean insertToLeft = false;
        int leftOffset = 50;
        int rightOffset = 0;
        int leftStartOffset = -MIN_LEFT_RIGHT_DIFF;
        int rightStartOffset = 0;
        
        int dayOffset = 0;

        if(!rightOnly) {
            leftColumn.add(generateSpacer(leftOffset));
        }

        for (DayAgreggator day : visitor.getDays()) {

            int lastOffset = 0;

            HtmlElement element = null;
            
            //for (Entry<Feature, Entries> e : day.getFeatures().entrySet()) {
            for (Entries<?> e : day.getEntries()) {
                if(e instanceof FeatureEntries) {
                    FeatureEntries f = (FeatureEntries) e;
                    EventFeatureComponent featureComponent = new EventFeatureComponent(f.getKey(), getLocalizator(), true);
                    for (HtmlEntry entry : e) {
                        featureComponent.add(entry);
                        
                        if(entry.getDate().after(lastWatchedEvents)) {
                            entry.setCssClass("unseen-entry");
                        }
                        
                    }
                    element = featureComponent;
                } else if (e instanceof BugEntries) {
                    continue;
                    //throw new NotImplementedException();
                }
                

                int blockHeight = 69 + 24 * e.size() + 30;
                element.addAttribute("style", "height: "+(blockHeight-30)+"px;");
                int offset;

                if (!rightOnly && leftOffset < rightOffset) {

                    if (leftOffset < dayOffset) {
                        leftColumn.add(generateSpacer(dayOffset - leftOffset));
                        leftOffset = dayOffset;
                    }
                    
                    if (leftOffset - rightStartOffset  < MIN_LEFT_RIGHT_DIFF) {
                        int space = MIN_LEFT_RIGHT_DIFF - (leftOffset - rightStartOffset);
                        leftColumn.add(generateSpacer(space));
                        leftOffset += space;
                    }

                    leftColumn.add(element);

                    offset = leftOffset;
                    leftStartOffset = offset;
                    leftOffset += blockHeight;
                } else {
                    if (rightOffset < dayOffset) {
                        rightColumn.add(generateSpacer(dayOffset - rightOffset));
                        rightOffset = dayOffset;
                    }

                    if (rightOffset - leftStartOffset  < MIN_LEFT_RIGHT_DIFF) {
                        int space = MIN_LEFT_RIGHT_DIFF - ( rightOffset - leftStartOffset);
                        leftColumn.add(generateSpacer(space));
                        rightOffset += space;
                    }
                    
                    rightColumn.add(element);

                    offset = rightOffset;
                    rightStartOffset = offset;
                    rightOffset += blockHeight;
                }

                if (offset > lastOffset) {
                    lastOffset = offset;
                }

                //insertToLeft = !insertToLeft;

            }

            int height;

            height = lastOffset - dayOffset + 70;

            if (height < MIN_DAY_HEIGHT) {
                height = MIN_DAY_HEIGHT;
            }

            dayOffset += height;

            daysPlaceHolder.add(generateDay(dayFormat.format(day.getDate().getTime()), height));

        }
    }

    private HtmlNode generateDay(String text, int height) {
        final HtmlDiv dayBlock = new HtmlDiv("day");
        dayBlock.addText(text);
        dayBlock.addAttribute("style", "height: " + height + "px;");
        return dayBlock;
    }

    private HtmlNode generateSpacer(int height) {
        final HtmlDiv dayBlock = new HtmlDiv("spacer");
        dayBlock.addAttribute("style", "height: " + height + "px;");
        return dayBlock;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return TimelinePage.generateBreadcrumb();
    }

    static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new TimelinePageUrl().getHtmlLink(tr("Timeline")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add see your timeline.");
    }
}
