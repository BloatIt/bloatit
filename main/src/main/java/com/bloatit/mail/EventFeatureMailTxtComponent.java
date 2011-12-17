package com.bloatit.mail;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.model.Feature;
import com.bloatit.web.url.FeaturePageAliasUrl;

public class EventFeatureMailTxtComponent {

    private static final int PROGRESS_BAR_SIZE = 50;
    private final StringBuilder sb = new StringBuilder();

    public EventFeatureMailTxtComponent(Feature f, Localizator l) {
        sb.append("┏━");
        switch (f.getFeatureState()) {
            case DEVELOPPING:
                generateProgressBar(f);
                sb.append(l.tr(" — Developing"));
                sb.append("\n");
                break;
            case FINISHED:
                generateProgressBar(f);
                sb.append(l.tr(" — Finished"));
                sb.append("\n");
                break;
            case PENDING:
                generateProgressBar(f);
                sb.append(l.tr(" — Waiting for a development offer"));
                sb.append("\n");
                break;
            case PREPARING:
                generateProgressBar(f);
                sb.append("\n");
                break;

            default:
                break;
        }
        sb.append("┃\n");
        sb.append("┃  \"");

        sb.append(f.getTitle(l.getLocale()));
        sb.append("\" — ");
        sb.append(new FeaturePageAliasUrl(f).externalUrlString());
        sb.append("\n");
    }

    private void generateProgressBar(Feature f) {
        int progression = ((Float) f.getProgression()).intValue();
        int nbEquals = ((progression * PROGRESS_BAR_SIZE) / 100);
        nbEquals = Math.min(PROGRESS_BAR_SIZE, nbEquals);
        if (nbEquals == 0) {
            sb.append("├");
        } else {
            sb.append("┣");
        }
        int i = 0;
        for (; i < nbEquals; i++) {
            sb.append("━");
        }
        if (i != 0 && i != PROGRESS_BAR_SIZE) {
            sb.append("▶");
            i++;
        }
        for (; i < PROGRESS_BAR_SIZE; i++) {
            sb.append("─");
        }
        if (nbEquals == PROGRESS_BAR_SIZE) {
            sb.append("┫ ");
        } else {
            sb.append("┤ ");
        }

        sb.append(f.getContribution().intValue());
        if (f.getSelectedOffer() != null) {
            sb.append("/");
            sb.append(f.getSelectedOffer().getAmount().intValue());
        }
        sb.append("€ ");

        if (progression != 0) {
            sb.append(progression);
            sb.append("%");
        }
    }

    public void addEntry(String entry) {
        sb.append("┃ ");
        sb.append(entry);
        sb.append("\n");
    }

    public String toPlainString() {
        return sb.toString();
    }
}
