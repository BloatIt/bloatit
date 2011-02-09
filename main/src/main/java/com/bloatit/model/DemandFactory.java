package com.bloatit.model;

import java.util.Locale;

import com.bloatit.model.demand.DemandImplementation;

public class DemandFactory {

    public static Demand createDemand(final Member author,
                                               final Locale locale,
                                               final String title,
                                               final String description,
                                               final Project project) {
        return new DemandImplementation(author, locale, title, description, project);
    }
}
