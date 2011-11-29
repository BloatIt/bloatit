package com.bloatit.mail;

import java.io.IOException;
import java.util.Map.Entry;

import com.bloatit.framework.feedbackworker.FeedBackWorker;
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
    protected boolean doWork(EventMailData data) {
        setLocal(data.getTo().getLocale());
        final MailEventVisitor visitor = new MailEventVisitor(getLocalizator());
        for (Event event : data) {
            event.getEvent().accept(visitor);
        }

        HtmlBranch html = new HtmlGenericElement("html");
        for (Entry<Feature, MailEventVisitor.Entries> e : visitor.getFeatures().entrySet()) {
            EventFeatureComponent featureComponent = new EventFeatureComponent(e.getKey(), getLocalizator());
            for (HtmlEntry entry : e.getValue()) {
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

}
