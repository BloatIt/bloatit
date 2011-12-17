package com.bloatit.mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;

public class ActivityEventVisitor extends GenericEventVisitor {

    private final LinkedList<DayAgreggator> days = new LinkedList<DayAgreggator>();

    public ActivityEventVisitor(final Localizator localizator) {
        super(localizator);
    }

    public List<DayAgreggator> getDays() {
        Collections.sort(days);
        Collections.reverse(days);
        return days;
    }

    @Override
    protected void addFeatureEntry(final Feature feature, final HtmlEntry entry, final Date date) {
        final DayAgreggator day = getDay(date);
        // addEntry(day.getFeatures(), f, b);
        day.addEntry(feature, entry, date);
    }

    private DayAgreggator getDay(final Date date) {

        final DayAgreggator newDayAgreggator = new DayAgreggator(date);

        for (final DayAgreggator day : days) {
            if (day.equals(newDayAgreggator)) {
                return day;
            }
        }
        days.addFirst(newDayAgreggator);
        return newDayAgreggator;
    }

    @Override
    protected void addBugEntry(final Bug bug, final HtmlEntry entry, final Date date) {
        final DayAgreggator day = getDay(date);
        // addEntry(day.getBugs(), f, b);
        day.addEntry(bug, entry, date);
    }

    /*
     * private <T> void addEntry(Map<T, Entries> m, T f, HtmlEntry b) { if
     * (m.containsKey(f)) { m.get(f).addFirst(b); } else { final Entries entries
     * = new Entries(); entries.addFirst(b); m.put(f, entries); } }
     */

    public class Entries<T> extends LinkedList<HtmlEntry> implements Comparable<Entries<?>> {
        private static final long serialVersionUID = 4240985577107981629L;

        private final T t;
        private Date lastDate;

        public Entries(final T t) {
            this.t = t;
        }

        public T getKey() {
            return t;
        }

        public void addEntry(final HtmlEntry entry, final Date date) {
            lastDate = date;
            addFirst(entry);
        }

        public Date getLastDate() {
            return lastDate;
        }

        @Override
        public int compareTo(final Entries<?> o) {
            return lastDate.compareTo(o.getLastDate());
        }
    }

    public class FeatureEntries extends Entries<Feature> {
        private static final long serialVersionUID = -390396466956297807L;

        public FeatureEntries(final Feature t) {
            super(t);
        }
    }

    public class BugEntries extends Entries<Bug> {
        private static final long serialVersionUID = -4591815606300887081L;

        public BugEntries(final Bug t) {
            super(t);
        }
    }

    public class DayAgreggator implements Comparable<DayAgreggator> {
        private final Calendar date;

        // private final Map<Feature, Entries<Feature>> features = new
        // HashMap<Feature, Entries<Feature>>();
        // private final Map<Bug, Entries<Bug>> bugs = new HashMap<Bug,
        // Entries<Bug>>();
        ArrayList<Entries<?>> entries = new ArrayList<Entries<?>>();

        public DayAgreggator(final Date date) {
            this.date = Calendar.getInstance();
            this.date.setTime(date);
            this.date.set(Calendar.HOUR_OF_DAY, 0);
            this.date.set(Calendar.MINUTE, 0);
            this.date.set(Calendar.SECOND, 0);
        }

        public void addEntry(final Bug bug, final HtmlEntry entry, final Date date) {
            for (final Entries<?> existingEntry : entries) {
                if (existingEntry.getKey().equals(bug)) {
                    existingEntry.addEntry(entry, date);
                    return;
                }
            }
            final BugEntries bugEntries = new BugEntries(bug);
            bugEntries.addEntry(entry, date);
            entries.add(bugEntries);
        }

        public void addEntry(final Feature feature, final HtmlEntry entry, final Date date) {
            for (final Entries<?> existingEntry : entries) {
                if (existingEntry.getKey().equals(feature)) {
                    existingEntry.addEntry(entry, date);
                    return;
                }
            }
            final FeatureEntries featureEntries = new FeatureEntries(feature);
            featureEntries.addEntry(entry, date);
            entries.add(featureEntries);
        }

        /*
         * public final Map<Feature, Entries<Feature>> getFeatures() { return
         * features; } public final Map<Bug, Entries<Bug>> getBugs() { return
         * bugs; }
         */

        public final ArrayList<Entries<?>> getEntries() {
            Collections.sort(entries);
            Collections.reverse(entries);
            return entries;
        }

        public Calendar getDate() {
            return date;
        }

        @Override
        public int hashCode() {
            final int result = 1;
            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DayAgreggator other = (DayAgreggator) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }

            return Math.abs(date.getTimeInMillis() - other.getDate().getTimeInMillis()) < 1000;
        }

        @Override
        public int compareTo(final DayAgreggator o) {
            return date.compareTo(o.getDate());
        }
        
        private ActivityEventVisitor getOuterType() {
            return ActivityEventVisitor.this;
        }

    }

}
