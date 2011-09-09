package com.bloatit.web.linkable.members.tabs.dashboard;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A dashboard is a simple list of Dashboard entries
 */
public class Dashboard implements Iterable<DashboardEntry> {
    ArrayList<DashboardEntry> entries;

    public Dashboard() {
        this.entries = new ArrayList<DashboardEntry>();
    }

    public void addEntry(DashboardEntry entry) {
        entries.add(entry);
    }

    @Override
    public Iterator<DashboardEntry> iterator() {
        return entries.iterator();
    }
}
