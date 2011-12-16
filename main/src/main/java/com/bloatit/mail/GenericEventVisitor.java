package com.bloatit.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlString;
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
import com.bloatit.model.Image;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.components.HtmlAuthorLink;
import com.bloatit.web.components.MoneyDisplayComponent;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.ReleasePageUrl;

public abstract class GenericEventVisitor implements EventVisitor<String> {

    private static final HtmlImage LOGO_FEATURE = new HtmlImage(new UrlString(WebConfiguration.getImgIdeaTiny()).externalUrlString());
    private static final HtmlImage LOGO_CONTRIBUTION = new HtmlImage(new UrlString(WebConfiguration.getImgContributionTiny()).externalUrlString());
    private static final HtmlImage LOGO_OFFER = new HtmlImage(new UrlString(WebConfiguration.getImgOfferTiny()).externalUrlString());
    private static final HtmlImage LOGO_COMMENT = new HtmlImage(new UrlString(WebConfiguration.getImgCommentTiny()).externalUrlString());
    private static final HtmlImage LOGO_RELEASE = new HtmlImage(new UrlString(WebConfiguration.getImgReleaseTiny()).externalUrlString());
    private static final HtmlImage LOGO_BUG = new HtmlImage(new UrlString(WebConfiguration.getImgBugTiny()).externalUrlString());
    
    protected final Locale locale;
    private final Localizator l;

    public GenericEventVisitor(final Localizator localizator) {
        this.locale = localizator.getLocale();
        this.l = localizator;
    }

    public class Entries extends ArrayList<HtmlEntry> {
        private static final long serialVersionUID = 4240985577107981629L;
    }

    protected abstract void addFeatureEntry(Feature f, HtmlEntry b, Date date);

    protected abstract void addBugEntry(Bug f, HtmlEntry b, Date date);

    @Override
    public String visit(final FeatureEvent event) {
        HtmlEntry entry;
        final FeaturePageUrl featureUrl = new FeaturePageUrl(event.getFeature(), FeatureTabKey.description);
        final HtmlBranch featureLink = new HtmlLink(featureUrl.externalUrlString(locale));
        switch (event.getType()) {
            case CREATE_FEATURE:
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("<0::+feature> created by <1:@thomas:>"),
                                                                                       featureLink,
                                                                                       new HtmlAuthorLink(event.getFeature())));
                break;
            case IN_DEVELOPING_STATE:
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("the <0::+feature> is now in development"), featureLink));
                break;
            case DISCARDED:
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("the <0::+feature> has been discarded"), featureLink));
                break;
            case FINICHED:
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("the <0::+feature> is now successful"), featureLink));
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final BugEvent event) {
        HtmlEntry entry;
        final BugPageUrl bugUrl = new BugPageUrl(event.getBug());
        switch (event.getType()) {
            case ADD_BUG:
                final HtmlLink bugLink = new HtmlLink(bugUrl.externalUrlString(locale));
                bugLink.setCssClass("bug_link");
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
                entry = new HtmlEntry(event.getDate(), LOGO_BUG, new PlaceHolderElement().add(new HtmlMixedText(Context.tr("new <0::+bug> ({0})",
                                                                                                                           criticity), bugLink)));
                addFeatureEntry(event.getFeature(), entry, event.getDate());
                break;
            case BUG_CHANGE_LEVEL:
                HtmlBranch bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                switch (event.getBug().getErrorLevel()) {
                    case FATAL:
                        entry = new HtmlEntry(event.getDate(), LOGO_BUG, new HtmlMixedText(l.tr("the <0::+bug> level is now to 'critical'"), bugLn));
                        break;
                    case MAJOR:
                        entry = new HtmlEntry(event.getDate(), LOGO_BUG, new HtmlMixedText(l.tr("the <0::+bug> level is now to 'major'"), bugLn));
                        break;
                    case MINOR:
                        entry = new HtmlEntry(event.getDate(), LOGO_BUG, new HtmlMixedText(l.tr("the <0::+bug> level is now 'minor'"), bugLn));
                        break;
                    default:
                        entry = new HtmlEntry(event.getDate(), LOGO_BUG, new HtmlMixedText(l.tr("the <0::+bug> is now in a new state"), bugLn));
                        break;
                }
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            case BUG_SET_RESOLVED:
                bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("the <0::+bug> is now resolved"), bugLn));
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            case BUG_SET_DEVELOPING:
                bugLn = new HtmlLink(bugUrl.externalUrlString(locale));
                entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new HtmlMixedText(l.tr("the <0::+bug> is being developed"), bugLn));
                addBugEntry(event.getBug(), entry, event.getDate());
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }

        return null;
    }

    @Override
    public String visit(final BugCommentEvent event) {
        final HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO_FEATURE, new PlaceHolderElement().addText(l.tr("1 new comment by "))
                                                                                                     .add(new HtmlAuthorLink(event.getComment())));
        addBugEntry(event.getBug(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final ContributionEvent event) {
        HtmlEntry entry;
        switch (event.getType()) {
            case ADD_CONTRIBUTION:
                entry = new HtmlEntry(event.getDate(),
                                      LOGO_CONTRIBUTION,
                                      new MoneyDisplayComponent(event.getContribution().getAmount(), l).addText(l.tr(" financed by "))
                                                                                                       .add(new HtmlAuthorLink(event.getContribution())));
                break;
            case REMOVE_CONTRIBUTION:
                entry = new HtmlEntry(event.getDate(), LOGO_CONTRIBUTION, new HtmlDiv().addText("contribution removed by")
                                                                                       .add(new HtmlAuthorLink(event.getContribution())));
                break;
            default:
                throw new BadProgrammerException("You should have managed all the cases.");
        }
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final FeatureCommentEvent event) {
        final HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO_COMMENT, new PlaceHolderElement().addText(l.tr("1 new comment by "))
                                                                                                     .add(new HtmlAuthorLink(event.getComment())));
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

    @Override
    public String visit(final OfferEvent event) {
        HtmlEntry entry;
        final FeaturePageUrl featureUrl = new FeaturePageUrl(event.getFeature(), FeatureTabKey.offers);
        final HtmlBranch offerLn = new HtmlLink(featureUrl.externalUrlString(locale));
        switch (event.getType()) {
            case ADD_OFFER:
                entry = new HtmlEntry(event.getDate(), LOGO_OFFER, new HtmlMixedText(l.tr("<0::+offer> created by <1:@thomas:>"),
                                                                                     offerLn,
                                                                                     new HtmlAuthorLink(event.getFeature())));
                break;
            case REMOVE_OFFER:
                entry = new HtmlEntry(event.getDate(), LOGO_OFFER, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> has been removed"),
                                                                                     offerLn,
                                                                                     new HtmlAuthorLink(event.getFeature())));
                break;
            case ADD_SELECTED_OFFER:
                entry = new HtmlEntry(event.getDate(), LOGO_OFFER, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> is selected"),
                                                                                     offerLn,
                                                                                     new HtmlAuthorLink(event.getFeature())));
                break;
            case CHANGE_SELECTED_OFFER:
                entry = new HtmlEntry(event.getDate(), LOGO_OFFER, new HtmlMixedText(l.tr("the <0::+offer> by <1:@thomas:> is selected"),
                                                                                     offerLn,
                                                                                     new HtmlAuthorLink(event.getFeature())));
                break;
            case REMOVE_SELECTED_OFFER:
                entry = new HtmlEntry(event.getDate(), LOGO_OFFER, "no offer selected");
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
        final String urlString = url.externalUrlString(locale);
        final HtmlEntry entry = new HtmlEntry(event.getDate(), LOGO_RELEASE, new HtmlMixedText(l.tr("new <0::+release> ({0})", event.getRelease()
                                                                                                                                    .getVersion()),
                                                                                               new HtmlLink(urlString)));
        addFeatureEntry(event.getFeature(), entry, event.getDate());
        return null;
    }

}
