package com.bloatit.mail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.feedbackworker.FeedBackWorker;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
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

public class EventDataworker extends FeedBackWorker<EventMailData> {

    public EventDataworker() {
        super(EventMailData.class);
    }

    @Override
    protected boolean doWork(EventMailData data) {
        System.out.println("Mail for: " + data.getTo());
        final MailEventVisitor visitor = new MailEventVisitor();
        for (Event event : data) {
            System.out.println("event : " + event.getId());
            event.getEvent().accept(visitor);
        }
        try {
            for (Entry<Feature, MailEventVisitor.Entries> e : visitor.getFeatures().entrySet()) {
                System.out.println("Feature - " + e.getKey().getId());
                for (MailEventVisitor.HtmlEntry line : e.getValue()) {
                    line.write(System.out);
                }
            }
            for (Entry<Bug, MailEventVisitor.Entries> e : visitor.getBugs().entrySet()) {
                System.out.println("Bug - " + e.getKey().getId());
                for (MailEventVisitor.HtmlEntry line : e.getValue()) {
                    line.write(System.out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static class MailEventVisitor implements EventVisitor<String> {

        private static final HtmlImage LOGO = new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "plop");

        private final Map<Feature, Entries> features = new HashMap<Feature, Entries>();
        private final Map<Bug, Entries> bugs = new HashMap<Bug, Entries>();

        public MailEventVisitor() {
            // Nothing ...
        }

        public final Map<Feature, Entries> getFeatures() {
            return features;
        }

        public final Map<Bug, Entries> getBugs() {
            return bugs;
        }

        private class Entries extends ArrayList<HtmlEntry> {
            private static final long serialVersionUID = 4240985577107981629L;
        }

        private class HtmlEntry extends HtmlDiv {
            public HtmlEntry(Date when, HtmlImage logo, String content) {
                this(when, logo, new HtmlText(content));
                add(logo);
                add(new HtmlSpan("date").addText(new SimpleDateFormat("HH-mm").format(when).toString()));
                addText(content);
            }

            public HtmlEntry(Date when, HtmlImage logo, HtmlNode content) {
                super("event-entry");
                add(logo);
                add(new HtmlSpan("date").addText(new SimpleDateFormat("HH-mm").format(when).toString()));
                add(content);
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
            switch (event.getType()) {
                case CREATE_FEATURE:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Feature creation.");
                    break;
                case IN_DEVELOPING_STATE:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "The feature is in development !");
                    break;
                case DISCARDED:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Discarded ...");
                    break;
                case FINICHED:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Success !");
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
            switch (event.getType()) {
                case ADD_BUG:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "New bug");
                    break;
                case BUG_CHANGE_LEVEL:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "New bug level");
                    break;
                case BUG_SET_RESOLVED:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "The bug is resolved");
                    break;
                case BUG_SET_DEVELOPING:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "The bug is in dev");
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(BugCommentEvent event) {
            HtmlEntry entry = new HtmlEntry(event.getCreationDate(), LOGO, "New comment on this bug !");
            addBugEntry(event.getBug(), entry);
            return null;
        }

        @Override
        public String visit(ContributionEvent event) {
            HtmlEntry entry;
            switch (event.getType()) {
                case ADD_CONTRIBUTION:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "New contribution");
                    break;
                case REMOVE_CONTRIBUTION:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Remove contribution");
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(FeatureCommentEvent event) {
            HtmlEntry entry = new HtmlEntry(event.getCreationDate(), LOGO, "New comment on this Feature !");
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(OfferEvent event) {
            HtmlEntry entry;
            switch (event.getType()) {
                case ADD_OFFER:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "New Offer");
                    break;
                case REMOVE_OFFER:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Remove offer");
                    break;
                case ADD_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "New Selected offer");
                    break;
                case CHANGE_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "Selected offer changed");
                    break;
                case REMOVE_SELECTED_OFFER:
                    entry = new HtmlEntry(event.getCreationDate(), LOGO, "No more selected offer");
                    break;
                default:
                    throw new BadProgrammerException("You should have managed all the cases.");
            }
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

        @Override
        public String visit(ReleaseEvent event) {
            HtmlEntry entry = new HtmlEntry(event.getCreationDate(), LOGO, "New release on this Feature !");
            addFeatureEntry(event.getFeature(), entry);
            return null;
        }

    }

}
