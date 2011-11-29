package com.bloatit.mail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.feedbackworker.FeedBackWorker;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.model.Bug;
import com.bloatit.model.Event;
import com.bloatit.model.Event.BugCommentEvent;
import com.bloatit.model.Event.BugEvent;
import com.bloatit.model.Event.ContributionEvent;
import com.bloatit.model.Event.EventVisitor;
import com.bloatit.model.Event.FeatureCommentEvent;
import com.bloatit.model.Event.FeatureEvent;
import com.bloatit.model.Event.OfferEvent;
import com.bloatit.model.Event.ReleaseEvent;
import com.bloatit.model.EventMailer.EventMailData;
import com.bloatit.model.Feature;
import com.bloatit.model.Image;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlAuthorLink;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.ReleasePageUrl;

public class EventDataworker extends FeedBackWorker<EventMailData> {

    public EventDataworker() {
        super(EventMailData.class);
    }

    @Override
    protected boolean doWork(EventMailData data) {
        setLocal(data.getTo().getLocale());
        final MailEventVisitor visitor = new MailEventVisitor(getLocalizator());
        for (Event event : data) {
            event.getEvent().accept(visitor);
        }

        HtmlBranch html = new HtmlGenericElement("html");
        for (Entry<Feature, MailEventVisitor.Entries> e : visitor.getFeatures().entrySet()) {
            EventFeatureComponent featureComponent = new EventFeatureComponent(e.getKey(), getLocalizator());
            for (MailEventVisitor.HtmlEntry entry : e.getValue()) {
                featureComponent.add(entry);
            }
            html.add(featureComponent);
        }

        try {
            html.write(System.out);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // for (Entry<Bug, MailEventVisitor.Entries> e :
        // visitor.getBugs().entrySet()) {
        // System.out.println("Bug - " + e.getKey().getId());
        // for (MailEventVisitor.HtmlEntry line : e.getValue()) {
        // line.write(System.out);
        // }
        // }
        return true;
    }

    public static class MailEventVisitor implements EventVisitor<String> {

        private static final HtmlImage LOGO = new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "plop");

        private final Map<Feature, Entries> features = new HashMap<Feature, Entries>();
        private final Map<Bug, Entries> bugs = new HashMap<Bug, Entries>();
        private final Locale locale;
        private final Localizator l;

        public MailEventVisitor(Localizator localizator) {
            this.locale = localizator.getLocale();
            this.l = localizator;
        }

        public final Map<Feature, Entries> getFeatures() {
            return features;
        }

        public final Map<Bug, Entries> getBugs() {
            return bugs;
        }

        public class Entries extends ArrayList<HtmlEntry> {
            private static final long serialVersionUID = 4240985577107981629L;
        }

        public class HtmlEntry extends HtmlDiv {
            public HtmlEntry(Date when, HtmlImage logo, String content) {
                this(when, logo, new HtmlText(content));
            }

            public HtmlEntry(Date when, HtmlImage logo, HtmlNode content) {
                super("event-entry");
                add(new HtmlDiv("event-entry-logo").add(logo));
                add(new HtmlDiv("event-entry-content").add(content).add(new HtmlSpan("date").addText(" - " + new SimpleDateFormat("HH:mm").format(when).toString())));
            }
        }

        private void addFeatureEntry(Feature f, HtmlEntry b) {
            addEntry(features, f, b);
        }

        private void addBugEntry(Bug f, HtmlEntry b) {
            addEntry(bugs, f, b);
        }

        private <T> void addEntry(Map<T, Entries> m, T f, HtmlEntry b) {
            if (m.containsKey(f)) {
                m.get(f).add(b);
            } else {
                final Entries entries = new Entries();
                entries.add(b);
                m.put(f, entries);
            }
        }

        @Override
        public String visit(FeatureEvent event) {
            HtmlEntry entry;
            FeaturePageUrl featureUrl = new FeaturePageUrl(event.getFeature(), FeatureTabKey.description);
            HtmlBranch featureLink = new HtmlLink(featureUrl.externalUrlString(locale));
            switch (event.getType()) {
                case CREATE_FEATURE:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("<0::+feature> created by <1:@thomas:>"),
                                                                                   featureLink,
                                                                                   new HtmlAuthorLink(event.getFeature())));
                    break;
                case IN_DEVELOPING_STATE:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+feature> is now in development"), featureLink));
                    break;
                case DISCARDED:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+feature> has been discarded"), featureLink));
                    break;
                case FINICHED:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+feature> is now successful"), featureLink));
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(BugEvent event) {
            HtmlEntry entry;
            BugPageUrl bugUrl = new BugPageUrl(event.getBug());
            switch (event.getType()) {
                case ADD_BUG:
                    HtmlBranch bugLink = new HtmlLink(bugUrl.externalUrlString(locale)).addText("+" + event.getBug().getTitle());
                    switch (event.getBug().getErrorLevel()) {
                        case FATAL:
                            entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("new critical bug: ")).add(bugLink));
                            break;
                        case MAJOR:
                            entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("new major bug: ")).add(bugLink));
                            break;
                        case MINOR:
                            entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("new minor bug: ")).add(bugLink));
                            break;
                        default:
                            entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("new bug: ")).add(bugLink));
                            break;
                    }
                    break;
                case BUG_CHANGE_LEVEL:
                    HtmlBranch bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                    switch (event.getBug().getErrorLevel()) {
                        case FATAL:
                            entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> level is now to 'critical'"), bugLn));
                            break;
                        case MAJOR:
                            entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> level is now to 'major'"), bugLn));
                            break;
                        case MINOR:
                            entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> level is now 'minor'"), bugLn));
                            break;
                        default:
                            entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> is now in a new state"), bugLn));
                            break;
                    }
                    break;
                case BUG_SET_RESOLVED:
                    bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> is now resolved"), bugLn));
                    break;
                case BUG_SET_DEVELOPING:
                    bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+bug> is being developed"), bugLn));
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(BugCommentEvent event) {
            HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("1 new comment by "))
                                                                                           .add(new HtmlAuthorLink(event.getComment())));
            addBugEntry(event.getBug(), entry);
            return null;
        }

        @Override
        public String visit(ContributionEvent event) {
            HtmlEntry entry;
            switch (event.getType()) {
                case ADD_CONTRIBUTION:
                    entry = new HtmlEntry(event.getDate(),
                                          LOGO,
                                          new MoneyDisplayComponent(event.getContribution().getAmount(), l).addText(l.tr(" financed by "))
                                                                                                        .add(new HtmlAuthorLink(event.getContribution())));
                    break;
                case REMOVE_CONTRIBUTION:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlDiv().addText("contribution removed by")
                                                                              .add(new HtmlAuthorLink(event.getContribution())));
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(FeatureCommentEvent event) {
            HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO, new PlaceHolderElement().addText(l.tr("1 new comment by "))
                                                                                           .add(new HtmlAuthorLink(event.getComment())));
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(OfferEvent event) {
            HtmlEntry entry;
            FeaturePageUrl featureUrl = new FeaturePageUrl(event.getFeature(), FeatureTabKey.offers);
            HtmlBranch offerLn = new HtmlLink(featureUrl.externalUrlString(locale));
            switch (event.getType()) {
                case ADD_OFFER:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("<0::+offer> created by <1:@thomas:>"),
                                                                                   offerLn,
                                                                                   new HtmlAuthorLink(event.getFeature())));
                    break;
                case REMOVE_OFFER:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> has been removed"),
                                                                                   offerLn,
                                                                                   new HtmlAuthorLink(event.getFeature())));
                    break;
                case ADD_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> is selected"),
                                                                                   offerLn,
                                                                                   new HtmlAuthorLink(event.getFeature())));
                    break;
                case CHANGE_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> is selected"),
                                                                                   offerLn,
                                                                                   new HtmlAuthorLink(event.getFeature())));
                    break;
                case REMOVE_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getDate(), LOGO, "no offer selected");
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(ReleaseEvent event) {
            ReleasePageUrl url = new ReleasePageUrl(event.getRelease());
            String urlString = url.externalUrlString(locale);
            HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO, new HtmlMixedText(l.tr("new <0::+release>"), new HtmlLink(urlString)));
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

    }

}
