package com.bloatit.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;

public class TimelineEventVisitor extends GenericEventVisitor {

    private final LinkedList<DayAgreggator> days = new LinkedList<DayAgreggator>();

    public TimelineEventVisitor(Localizator localizator) {
        super(localizator);
    }

    public List<DayAgreggator> getDays() {
        return days;
    }

    @Override
    protected void addFeatureEntry(Feature f, HtmlEntry b, Date date) {
        DayAgreggator day = getDay(date);
        addEntry(day.getFeatures(), f, b);
    }

    private DayAgreggator getDay(Date date) {

        DayAgreggator newDayAgreggator = new DayAgreggator(date);

        for (DayAgreggator day : days) {
            if (day.equals(newDayAgreggator)) {
                int i = 0;
                return day;
            }
        }
        days.addFirst(newDayAgreggator);
        return newDayAgreggator;
    }

    @Override
    protected void addBugEntry(Bug f, HtmlEntry b, Date date) {
        DayAgreggator day = getDay(date);
        addEntry(day.getBugs(), f, b);
    }

    private <T> void addEntry(Map<T, Entries> m, T f, HtmlEntry b) {
        if (m.containsKey(f)) {
            m.get(f).addFirst(b);
        } else {
            final Entries entries = new Entries();
            entries.addFirst(b);
            m.put(f, entries);
        }
    }

    public class Entries extends LinkedList<HtmlEntry> {
        private static final long serialVersionUID = 4240985577107981629L;
    }

    public class DayAgreggator {
        private final Calendar date;
        private final Map<Feature, Entries> features = new HashMap<Feature, Entries>();
        private final Map<Bug, Entries> bugs = new HashMap<Bug, Entries>();

        public DayAgreggator(Date date) {
            this.date = GregorianCalendar.getInstance();
            this.date.setTime(date);
            this.date.set(Calendar.HOUR_OF_DAY, 0);
            this.date.set(Calendar.MINUTE, 0);
            this.date.set(Calendar.SECOND, 0);
        }

        public final Map<Feature, Entries> getFeatures() {
            return features;
        }

        public final Map<Bug, Entries> getBugs() {
            return bugs;
        }

        public Calendar getDate() {
            return date;
        }

        @Override
        public int hashCode() {
            int result = 1;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DayAgreggator other = (DayAgreggator) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;

            return Math.abs(date.getTimeInMillis() - other.getDate().getTimeInMillis()) < 1000;
        }

        private TimelineEventVisitor getOuterType() {
            return TimelineEventVisitor.this;
        }

    }

}
