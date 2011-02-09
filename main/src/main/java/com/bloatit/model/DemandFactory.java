package com.bloatit.model;

import java.util.Locale;

import com.bloatit.model.demand.Demand;

public class DemandFactory {

    public static DemandInterface createDemand(final Member author,
                                               final Locale locale,
                                               final String title,
                                               final String description,
                                               final Project project) {
        return new Demand(author, locale, title, description, project);
    }
}
