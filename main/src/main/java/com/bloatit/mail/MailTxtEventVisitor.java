package com.bloatit.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.model.Event.BugCommentEvent;
import com.bloatit.model.Event.BugEvent;
import com.bloatit.model.Event.ContributionEvent;
import com.bloatit.model.Event.EventVisitor;
import com.bloatit.model.Event.FeatureCommentEvent;
import com.bloatit.model.Event.FeatureEvent;
import com.bloatit.model.Event.OfferEvent;
import com.bloatit.model.Event.ReleaseEvent;
import com.bloatit.model.Feature;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FeaturePageAliasUrl;
import com.bloatit.web.url.ReleasePageUrl;

public abstract class MailTxtEventVisitor implements EventVisitor<String> {

    protected final Locale locale;
    private final Localizator l;

    public MailTxtEventVisitor(final Localizator localizator) {
        this.locale = localizator.getLocale();
        this.l = localizator;
    }

    public class Entries extends ArrayList<TxtEntry> {
        private static final long serialVersionUID = 4240985577107981629L;
    }

    protected abstract void addFeatureEntry(Feature f, TxtEntry b, Date date);

    protected abstract void addBugEntry(Bug f, TxtEntry b, Date date);

    @Override
    public String visit(final FeatureEvent event) {
        TxtEntry entry;
        switch (event.getType()) {
            case CREATE_FEATURE:
                entry = new TxtEntry(event.getDate(), l.tr("feature created by {0}", event.getFeature().getAuthor().getDisplayName()), l);
                break;
            case IN_DEVELOPING_STATE:
                entry = new TxtEntry(event.getDate(), l.tr("the feature is now in development"), l);
                break;
            case DISCARDED:
                entry = new TxtEntry(event.getDate(), l.tr("the feature has been discarded"), l);
                break;
            case FINICHED:
                entry = new TxtEntry(event.getDate(), l.tr("the feature is now successful"), l);
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final BugEvent event) {
        TxtEntry entry;
        final BugPageUrl bugUrl = new BugPageUrl(event.getBug());
        switch (event.getType()) {
            case ADD_BUG:
                String criticity = "";
                switch (event.getBug().getErrorLevel()) {
                    case FATAL:
                        criticity = Context.tr("critical");
                        break;
                    case MAJOR:
                        criticity = Context.tr("major");
                        break;
                    case MINOR:
                        criticity = Context.tr("minor");
                        break;
                    default:
                        criticity = Context.tr("strange");
                        break;
                }
                entry = new TxtEntry(event.getDate(), Context.tr("new bug ({0})", criticity), l, bugUrl);
                addFeatureEntry(event.getFeature(), entry, event.getDate());
                break;
            case BUG_CHANGE_LEVEL:
                switch (event.getBug().getErrorLevel()) {
                    case FATAL:
                        entry = new TxtEntry(event.getDate(), l.tr("the bug level is now to 'critical'"), l, bugUrl);
                        break;
                    case MAJOR:
                        entry = new TxtEntry(event.getDate(), l.tr("the bug level is now to 'major'"), l, bugUrl);
                        break;
                    case MINOR:
                        entry = new TxtEntry(event.getDate(), l.tr("the bug level is now 'minor'"), l, bugUrl);
                        break;
                    default:
                        entry = new TxtEntry(event.getDate(), l.tr("the bug is now in a new state"), l, bugUrl);
                        break;
                }
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            case BUG_SET_RESOLVED:
                entry = new TxtEntry(event.getDate(), l.tr("the bug is now resolved"), l, bugUrl);
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            case BUG_SET_DEVELOPING:
                entry = new TxtEntry(event.getDate(), l.tr("the bug is being developed"), l, bugUrl);
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }

        return null;
    }

    @Override
    public String visit(final BugCommentEvent event) {
        final TxtEntry entry = new TxtEntry(event.getDate(), l.tr("1 new comment by {0}: ", new BugPageUrl(event.getBug())), l);
        addBugEntry(event.getBug(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final ContributionEvent event) {
        TxtEntry entry;
        switch (event.getType()) {
            case ADD_CONTRIBUTION:
                entry = new TxtEntry(event.getDate(), l.tr("{0}€ financed by {1}",
                                                           event.getContribution().getAmount().intValue(),
                                                           event.getContribution().getAuthor().getDisplayName()), l);
                break;
            case REMOVE_CONTRIBUTION:
                entry = new TxtEntry(event.getDate(), l.tr("contribution removed by {0}", event.getContribution().getAuthor().getDisplayName()), l);
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final FeatureCommentEvent event) {
        final TxtEntry entry = new TxtEntry(event.getDate(), l.tr("1 new comment by {0}", event.getComment().getAuthor().getDisplayName()), l);
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final OfferEvent event) {
        TxtEntry entry;
        final Url featureUrl = new FeaturePageAliasUrl(event.getFeature());
        switch (event.getType()) {
            case ADD_OFFER:
                entry = new TxtEntry(event.getDate(), l.tr("offer created by {0}", event.getFeature().getAuthor().getDisplayName()), l);
                break;
            case REMOVE_OFFER:
                entry = new TxtEntry(event.getDate(),
                                     l.tr("the offer by {0} has been removed: ", event.getFeature().getAuthor().getDisplayName()),
                                     l,
                                     featureUrl);
                break;
            case ADD_SELECTED_OFFER:
            case CHANGE_SELECTED_OFFER:
                entry = new TxtEntry(event.getDate(),
                                     l.tr("the offer by {0} is selected: ", event.getFeature().getAuthor().getDisplayName()),
                                     l,
                                     featureUrl);
                break;
            case REMOVE_SELECTED_OFFER:
                entry = new TxtEntry(event.getDate(), "no offer selected", l);
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final ReleaseEvent event) {
        final ReleasePageUrl url = new ReleasePageUrl(event.getRelease());
        final TxtEntry entry = new TxtEntry(event.getDate(), l.tr("new release ({0})", event.getRelease().getVersion()), l, url);
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }
}
