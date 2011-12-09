package com.bloatit.model;

import java.util.ArrayList;
import java.util.Date;
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
        // TODO :  remove in production !!!
        
        new RecursiveMailer(11 , EmailStrategy.VERY_FREQUENTLY, DateUtils.now());
        new RecursiveMailer(61 , EmailStrategy.HOURLY, DateUtils.now());
        new RecursiveMailer(120, EmailStrategy.DAILY, DateUtils.now());
        new RecursiveMailer(200, EmailStrategy.WEEKLY, DateUtils.now());
        
        /*new RecursiveMailer(11 * 60, EmailStrategy.VERY_FREQUENTLY);
        new RecursiveMailer(61 * 60, EmailStrategy.HOURLY);
        new RecursiveMailer(24 * 3601, EmailStrategy.DAILY);
        new RecursiveMailer(7 * 24 * 3599, EmailStrategy.WEEKLY);*/
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
        
        //Storing the last date permit to avoid duplicate sending or lost event
        private Date lastDate;

        /**
         * @param periode in seconds
         */
        public RecursiveMailer(int periode, DaoMember.EmailStrategy strategy, Date lastDate) {
            super(DateUtils.nowPlusSomeSeconds(periode), periode);
            this.periode = periode; 
            this.strategy = strategy;
            this.lastDate = lastDate;
        }

        @Override
        public void doRun() {
            Date currentDate = DateUtils.now();
            new RecursiveMailer(periode, strategy, currentDate);
            
            EventList eventList = EventManager.getAllEventAfter(lastDate, currentDate, strategy);
            lastDate = currentDate;
            
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
            
            // Flush
            if (data != null) {
                FeedbackServer.getInstance().sendMessage(data);
            }

        }
    }
}
