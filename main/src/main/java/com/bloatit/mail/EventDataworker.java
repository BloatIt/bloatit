package com.bloatit.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.common.TemplateFile;
import com.bloatit.framework.feedbackworker.FeedBackWorker;
import com.bloatit.framework.mailsender.Mail;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
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

        HtmlBranch html = new HtmlGenericElement("html");
        for (Entry<Feature, MailEventVisitor.Entries> e : visitor.getFeatures().entrySet()) {
            EventFeatureMailComponent featureComponent = new EventFeatureMailComponent(e.getKey(), getLocalizator());
            for (HtmlEntry entry : e.getValue()) {
                featureComponent.add(entry.generateForMail());
            }
            html.add(featureComponent);
        }

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            html.write(buffer);
           
            final Mail mail = new Mail(data.getTo().getEmailUnprotected(), 
                                       getLocalizator().trn("Elveos activity – {0} new event", "Elveos activity – {0} new events", eventCount, eventCount),
                                       new String(buffer.toByteArray()),
                                       "activity-feed");
            mail.setMimeType("text/html");
            MailServer.getInstance().send(mail);

        } catch (final IOException e) {
            Log.mail().fatal("Fail to generate activity email", e);
        }
        return true;
    }

}
