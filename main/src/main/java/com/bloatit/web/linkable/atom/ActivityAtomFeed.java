package com.bloatit.web.linkable.atom;

import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Event;
import com.bloatit.model.Event.BugCommentEvent;
import com.bloatit.model.Event.BugEvent;
import com.bloatit.model.Event.ContributionEvent;
import com.bloatit.model.Event.EventVisitor;
import com.bloatit.model.Event.FeatureCommentEvent;
import com.bloatit.model.Event.FeatureEvent;
import com.bloatit.model.Event.OfferEvent;
import com.bloatit.model.Event.ReleaseEvent;
import com.bloatit.model.Member;
import com.bloatit.model.managers.EventManager;
import com.bloatit.web.linkable.atom.master.ElveosAtomFeed;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.url.ActivityAtomFeedUrl;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * A feed used to display the recent features related to a given software
 */
@ParamContainer("activityfeed")
public class ActivityAtomFeed extends ElveosAtomFeed {

    private Date updateDate;

    @NonOptional(@tr("You have to specify a member number."))
    @RequestParam(role = Role.GET, message = @tr("I cannot find the member number: ''%value%''."))
    private final Member member;

    public ActivityAtomFeed(final ActivityAtomFeedUrl url) {
        super(url);
        this.member = url.getMember();
    }

    @Override
    public void generate() {
        boolean first = true;

        PageIterable<Event> allEventByMember = EventManager.getAllEventByMember(member);
        allEventByMember.setPageSize(100);
        for (Event event : allEventByMember) {
            addFeedEntry(event.getEvent().accept(new AtomVisitor()), Position.FIRST);
            if (first) {
                updateDate = event.getEvent().getDate();
                first = false;
            }
        }
    }

    @Override
    public String getFeedTitle() {
        return Context.tr("Elveos feature feed for {0}''s activity.", member.getDisplayName());
    }

    @Override
    public Date getUpdatedDate() {
        if (updateDate != null) {
            return updateDate;
        }
        return new Date();
    }

    private final class AtomVisitor implements EventVisitor<FeedEntry> {
        @Override
        public FeedEntry visit(final FeatureEvent event) {

            Actor<?> author;
            Url authorLink;
            if (event.getFeature().getAsTeam() == null) {
                author = event.getFeature().getMember();
                authorLink = new MemberPageUrl(event.getFeature().getMember());
            } else {
                author = event.getFeature().getAsTeam();
                authorLink = new TeamPageUrl(event.getFeature().getAsTeam());
            }

            String title = "";

            switch (event.getType()) {
                case CREATE_FEATURE:
                    title = Context.tr("Feature request created by {0}", author.getDisplayName());
                    break;
                case IN_DEVELOPING_STATE:
                    title = Context.tr("The feature is now in development");
                    break;
                case DISCARDED:
                    title = Context.tr("The feature has been discarded");
                    break;
                case FINICHED:
                    title = Context.tr("The feature is now successful");
                    break;
                default:
                    throw new NotImplementedException();
            }

            String softName = "";
			if (event.getFeature().getSoftware() != null){
            	softName = " – " + event.getFeature().getSoftware().getName();
            }
			return new FeedEntry(title + " – " + softName + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new FeaturePageUrl(event.getFeature(), FeatureTabKey.description).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }

        @Override
        public FeedEntry visit(final BugEvent event) {

            Actor<?> author;
            Url authorLink;
            if (event.getBug().getAsTeam() == null) {
                author = event.getBug().getMember();
                authorLink = new MemberPageUrl(event.getBug().getMember());
            } else {
                author = event.getBug().getAsTeam();
                authorLink = new TeamPageUrl(event.getBug().getAsTeam());
            }

            String title = "";

            switch (event.getType()) {
                case ADD_BUG:
                    title = Context.tr("New bug reported by {0}", author.getDisplayName());
                    break;
                case BUG_CHANGE_LEVEL:
                    title = Context.tr("A bug level changed");
                    break;
                case BUG_SET_DEVELOPING:
                    title = Context.tr("A bug is now in development");
                    break;
                case BUG_SET_RESOLVED:
                    title = Context.tr("A bug is now resolved");
                    break;
                default:
                    throw new NotImplementedException();
            }

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new BugPageUrl(event.getBug()).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }

        @Override
        public FeedEntry visit(final BugCommentEvent event) {
            Actor<?> author;
            Url authorLink;
            if (event.getComment().getAsTeam() == null) {
                author = event.getComment().getMember();
                authorLink = new MemberPageUrl(event.getComment().getMember());
            } else {
                author = event.getComment().getAsTeam();
                authorLink = new TeamPageUrl(event.getComment().getAsTeam());
            }

            final String title = Context.tr("New bug comment by {0}", author.getDisplayName());

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new BugPageUrl(event.getBug()).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }

        @Override
        public FeedEntry visit(final ContributionEvent event) {
            Actor<?> author;
            Url authorLink;
            if (event.getContribution().getAsTeam() == null) {
                author = event.getContribution().getMember();
                authorLink = new MemberPageUrl(event.getContribution().getMember());
            } else {
                author = event.getContribution().getAsTeam();
                authorLink = new TeamPageUrl(event.getContribution().getAsTeam());
            }

            String title = "";

            switch (event.getType()) {
                case ADD_CONTRIBUTION:
                    title = Context.tr("{0} € financed by {1}", event.getContribution().getAmount(), author.getDisplayName());
                    break;
                case REMOVE_CONTRIBUTION:
                    title = Context.tr("Contribution by {0} has been removed", author.getDisplayName());
                    break;
                default:
                    throw new NotImplementedException();
            }

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new FeaturePageUrl(event.getFeature(), FeatureTabKey.contributions).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }

        @Override
        public FeedEntry visit(final FeatureCommentEvent event) {
            Actor<?> author;
            Url authorLink;
            if (event.getComment().getAsTeam() == null) {
                author = event.getComment().getMember();
                authorLink = new MemberPageUrl(event.getComment().getMember());
            } else {
                author = event.getComment().getAsTeam();
                authorLink = new TeamPageUrl(event.getComment().getAsTeam());
            }

            final String title = Context.tr("New comment by {0}", author.getDisplayName());

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new FeaturePageUrl(event.getFeature(), FeatureTabKey.description).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }

        @Override
        public FeedEntry visit(final OfferEvent event) {

            Actor<?> author;
            Url authorLink;
            if (event.getOffer().getAsTeam() == null) {
                author = event.getOffer().getMember();
                authorLink = new MemberPageUrl(event.getOffer().getMember());
            } else {
                author = event.getOffer().getAsTeam();
                authorLink = new TeamPageUrl(event.getOffer().getAsTeam());
            }

            String title = "";

            switch (event.getType()) {
                case ADD_OFFER:
                    title = Context.tr("Offer created by {0}", author.getDisplayName());
                    break;
                case REMOVE_OFFER:
                    title = Context.tr("The offer by {0} has been removed", author.getDisplayName());
                    break;
                case ADD_SELECTED_OFFER:
                    title = Context.tr("The offer by {0} is selected", author.getDisplayName());
                    break;
                case CHANGE_SELECTED_OFFER:
                    title = Context.tr("The offer by {0} is selected", author.getDisplayName());
                    break;
                case REMOVE_SELECTED_OFFER:
                    title = Context.tr("No selected offer");
                    break;
                default:
                    throw new NotImplementedException();
            }

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new FeaturePageUrl(event.getFeature(), FeatureTabKey.offers).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());

        }

        @Override
        public FeedEntry visit(final ReleaseEvent event) {
            Actor<?> author;
            Url authorLink;
            if (event.getRelease().getAsTeam() == null) {
                author = event.getRelease().getMember();
                authorLink = new MemberPageUrl(event.getRelease().getMember());
            } else {
                author = event.getRelease().getAsTeam();
                authorLink = new TeamPageUrl(event.getRelease().getAsTeam());
            }

            final String title = Context.tr("New release by {0}", author.getDisplayName());

            return new FeedEntry(title + " – " + event.getFeature().getSoftware().getName() + " – " + FeaturesTools.getTitle(event.getFeature()),
                                 new FeaturePageUrl(event.getFeature(), FeatureTabKey.offers).externalUrlString(),
                                 "elveos-event-" + event.getDate().toString(),
                                 event.getDate(),
                                 "",
                                 author.getDisplayName(),
                                 authorLink.externalUrlString());
        }
    }
}
