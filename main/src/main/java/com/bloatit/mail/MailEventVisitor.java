package com.bloatit.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;

public class MailEventVisitor extends GenericEventVisitor {

    private final Map<Feature, Entries> features = new HashMap<Feature, Entries>();
    private final Map<Bug, Entries> bugs = new HashMap<Bug, Entries>();

    public MailEventVisitor(final Localizator localizator) {
        super(localizator);
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

    @Override
    protected void addFeatureEntry(final Feature f, final HtmlEntry b, final Date date) {
        addEntry(features, f, b);
    }

    @Override
    protected void addBugEntry(final Bug f, final HtmlEntry b, final Date date) {
        addEntry(bugs, f, b);
    }

    private <T> void addEntry(final Map<T, Entries> m, final T f, final HtmlEntry b) {
        if (m.containsKey(f)) {
            m.get(f).add(b);
        } else {
            final Entries entries = new Entries();
            entries.add(b);
            m.put(f, entries);
        }
    }

}
