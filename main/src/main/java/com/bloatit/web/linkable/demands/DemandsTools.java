package com.bloatit.web.linkable.demands;

import java.util.Locale;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.model.Demand;
import com.bloatit.model.Translation;

public class DemandsTools {

    public static String getTitle(Demand demand) throws UnauthorizedOperationException {
        final Locale defaultLocale = Context.getLocalizator().getLocale();
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        return translatedDescription.getTitle();
    }
}
