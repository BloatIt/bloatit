package com.bloatit.model;

import com.bloatit.data.DaoHighlightDemand;

public class HighlightDemand extends Identifiable<DaoHighlightDemand> {

    public static HighlightDemand create(final DaoHighlightDemand dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoHighlightDemand> created = CacheManager.get(dao);
            if (created == null) {
                return new HighlightDemand(dao);
            }
            return (HighlightDemand) created;
        }
        return null;
    }

    private HighlightDemand(DaoHighlightDemand dao) {
        super(dao);
    }


}
