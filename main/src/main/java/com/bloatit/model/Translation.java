package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoTranslation;
import com.bloatit.framework.exceptions.FatalErrorException;

public final class Translation extends Kudosable<DaoTranslation> {

    public static Translation create(final DaoTranslation dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoTranslation> created = CacheManager.get(dao);
            if (created == null) {
                return new Translation(dao);
            }
            return (Translation) created;
        }
        return null;
    }

    private Translation(final DaoTranslation dao) {
        super(dao);
    }

    public String getTitle() {
        return getDao().getTitle();
    }

    public Locale getLocale() {
        return getDao().getLocale();
    }

    public String getText() {
        return getDao().getText();
    }

    /**
     * Smart cut the text, add a "…" char, and return it.
     *
     * @param sizeMax is the maximum size the returned text can be.
     * @param variance is how far we are looking for the punctuation mark to cut the text.
     * @return a cut version of the text, find a point or a punctuation mark to cut it at
     *         the best position possible.
     */
    public String getShortText(final int sizeMax, final int variance) {
        if (variance < 0) {
            throw new FatalErrorException("variance must be >= 0");
        }
        final String wholeText = getDao().getText();
        if (wholeText.length() <= sizeMax) {
            return wholeText;
        }

        // Try the common punctuation marks.
        for (int i = sizeMax; i > sizeMax - variance; --i) {
            switch (wholeText.charAt(i - 1)) {
            case ',':
            case ';':
            case '!':
            case '?':
            case ':':
                return performTheCutAndAddPoints(sizeMax, wholeText, i);
            default:
                // Do nothing.
                break;
            }
        }

        // Try with a space or line return.
        for (int i = sizeMax; i > sizeMax - variance; --i) {
            switch (wholeText.charAt(i - 1)) {
            case ' ':
            case '\n':
            case '\r':
                return performTheCutAndAddPoints(sizeMax, wholeText, i);
            default:
                // Do nothing.
                break;
            }
        }

        // Found no good chars...
        return wholeText.substring(0, sizeMax) + "…";
    }

    private String performTheCutAndAddPoints(final int sizeMax, final String wholeText, final int i) {
        if (i < (sizeMax - 1)) {
            return wholeText.substring(0, i) + " …";
        }
        return wholeText.substring(0, i - 1) + "…";
    }

    // ////////////////////////////////////////////////////////////////////////
    // kudosable configuration
    // ////////////////////////////////////////////////////////////////////////

    /**
     * @see com.bloatit.model.Kudosable#turnPending()
     */
    @Override
    protected int turnPending() {
        return KudosableConfiguration.getTranslationTurnPending();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnValid()
     */
    @Override
    protected int turnValid() {
        return KudosableConfiguration.getTranslationTurnValid();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnRejected()
     */
    @Override
    protected int turnRejected() {
        return KudosableConfiguration.getTranslationTurnRejected();
    }

    /**
     * @see com.bloatit.model.Kudosable#turnHidden()
     */
    @Override
    protected int turnHidden() {
        return KudosableConfiguration.getTranslationTurnHidden();
    }

}
