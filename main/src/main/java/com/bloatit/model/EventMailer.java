package com.bloatit.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.EmailStrategy;
import com.bloatit.framework.feedbackworker.FeedbackServer;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.model.managers.EventManager;
import com.bloatit.model.managers.EventManager.EventList;

public final class EventMailer {

    private EventMailer() {
        // disactivate the constructor
    }

    public static void start() {
        new RecursiveMailer(11 * 60, EmailStrategy.VERY_FREQUENTLY);
        new RecursiveMailer(61 * 60, EmailStrategy.HOURLY);
        new RecursiveMailer(24 * 3601, EmailStrategy.DAILY);
        new RecursiveMailer(7 * 24 * 3599, EmailStrategy.WEEKLY);
    }

    public static class EventMailData implements Iterable<Event> {
        private final Member to;
        private final List<Event> events = new ArrayList<Event>();

        public EventMailData(Member to) {
            this.to = to;
        }

        protected void addEvent(Event event) {
            events.add(event);
        }

        @Override
        public Iterator<Event> iterator() {
            return events.iterator();
        }

        public Member getTo() {
            return to;
        }
    }

    private static class RecursiveMailer extends PlannedTask {

        private static final long serialVersionUID = 2566496487502210170L;
        private final int periode;
        private final EmailStrategy strategy;

        /**
         * @param periode in seconds
         */
        public RecursiveMailer(int periode, DaoMember.EmailStrategy strategy) {
            super(DateUtils.nowPlusSomeSeconds(periode), periode);
            this.periode = periode;
            this.strategy = strategy;
        }

        @Override
        public void doRun() {
            new RecursiveMailer(periode, strategy);
            EventList eventList = EventManager.getAllEventAfter(DateUtils.nowMinusSomeSeconds(periode), strategy);

            Member currentMember = null;
            EventMailData data = null;

            while (eventList.hasNext()) {
                eventList.next();
                if (currentMember != eventList.member()) {
                    if (data != null) {
                        FeedbackServer.getInstance().sendMessage(data);
                    }
                    data = new EventMailData(eventList.member());
                    currentMember = eventList.member();
                }
                data.addEvent(eventList.event());
            }

        }
    }
}
