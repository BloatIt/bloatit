package com.bloatit.mail;

import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.feedbackworker.FeedBackWorker;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.model.Event;
import com.bloatit.model.EventMailer.EventMailData;
import com.bloatit.model.Feature;

public class EventDataworker extends FeedBackWorker<EventMailData> {

    public EventDataworker() {
        super(EventMailData.class);
    }

    @Override
    protected boolean doWork(final EventMailData data) {
        setLocal(data.getTo().getLocale());
        final MailEventVisitor visitor = new MailEventVisitor(getLocalizator());
        int eventCount = 0;
        for (final Event event : data) {
            event.getEvent().accept(visitor);
            eventCount++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Hello,\n");
        sb.append("\n");
        sb.append("Here is your elveos reporting: \n\n");
        int oldLength = sb.length();

        for (Entry<Feature, MailEventVisitor.Entries> e : visitor.getFeatures().entrySet()) {
            EventFeatureMailTxtComponent featureComponent = new EventFeatureMailTxtComponent(e.getKey(), getLocalizator());
            for (TxtEntry entry : e.getValue()) {
                featureComponent.addEntry(entry.generate());
            }
            sb.append(featureComponent.toPlainString());
            sb.append("\n\n");
        }

        if (sb.length() == oldLength) {
            sb.append("Nothing to report !");
            Log.web().warn("Nothing to report in a sent mail from activity feed. It should never append!");
        }

        sb.append("Sincerly,\n");
        sb.append("The Elveos team\n");
        sb.append("\n");
        sb.append("Ps: You received this email because you follow some elveos feature requests.\n");
        sb.append("    You can manage them here: https://elveos.org/activity/settings");
        sb.append("\n");

        final Mail mail = new Mail(data.getTo().getEmailUnprotected(), getLocalizator().trn("Elveos activity – {0} new event",
                                                                                            "Elveos activity – {0} new events",
                                                                                            eventCount,
                                                                                            eventCount), sb.toString(), "activity-feed");
        mail.setMimeType("text/plain; charset=UTF-8");
        MailServer.getInstance().send(mail);
        return true;
    }
}
