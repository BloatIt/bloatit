package com.bloatit.framework;

import java.util.Locale;

import com.bloatit.common.FatalErrorException;
import com.bloatit.model.data.DaoKudosable;
import com.bloatit.model.data.DaoTranslation;

public final class Translation extends Kudosable {

    private final DaoTranslation dao;

    public static Translation create(final DaoTranslation dao) {
        if (dao == null) {
            return null;
        }
        return new Translation(dao);
    }

    private Translation(final DaoTranslation dao) {
        super();
        this.dao = dao;
    }

    public DaoTranslation getDao() {
        return dao;
    }

    public String getTitle() {
        return dao.getTitle();
    }

    public Locale getLocale() {
        return dao.getLocale();
    }

    public String getText() {
        return dao.getText();
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
        final String wholeText = dao.getText();
        if (wholeText.length() <= sizeMax) {
            return wholeText;
        }

        for (int i = sizeMax; i > sizeMax - variance; --i) {
            switch (wholeText.charAt(i - 1)) {
            case ',':
            case ';':
            case '!':
            case '?':
            case ':':
                return wholeText.substring(0, i - 1) + "…";
            default:
                // Do nothing.
                break;
            }
        }
        return wholeText.substring(0, sizeMax) + "…";
    }

    @Override
    protected DaoKudosable getDaoKudosable() {
        return dao;
    }

}
