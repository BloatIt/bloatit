package com.bloatit.web.linkable.atom;

import java.util.Date;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.cache.MemoryCache;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.Software;
import com.bloatit.model.Translation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.atom.master.ElveosAtomFeed;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.SoftwareAtomFeedUrl;

/**
 * A feed used to display the recent features related to a given software
 */
@ParamContainer("softwarefeed/%software%")
public class SoftwareAtomFeed extends ElveosAtomFeed {
    private Date updateDate;

    @NonOptional(@tr("You have to specify a software number."))
    @RequestParam(role = Role.PAGENAME, message = @tr("I cannot find the software number: ''%value%''."))
    private final Software software;

    public SoftwareAtomFeed(SoftwareAtomFeedUrl url) {
        super(url);
        this.software = url.getSoftware();
    }

    @Override
    public void generate() {
        boolean first = true;
        for (Feature feature : software.getFeaturesByCreationDate()) {
            Translation translation = feature.getDescription().getTranslationOrDefault(Language.fromLocale(Context.getLocalizator().getLocale()));
            String featureTitle = translation.getTitle();
            Software software = feature.getSoftware();
            String title;
            if (software == null) {
                title = Context.tr("New software") + " – " + featureTitle;
            } else {
                title = software.getName() + " – " + featureTitle;
            }

            String translationText = MemoryCache.getInstance().get(translation.getText());
            if (translationText == null) {
                final MarkdownParser parser = new MarkdownParser();
                try {
                    translationText = parser.parse(HtmlTools.escape(translation.getText()));
                    MemoryCache.getInstance().cache(translation.getText(), translationText);
                } catch (final ParsingException e) {
                    throw new BadProgrammerException("An error occured during markdown parsing", e);
                }
            }

            FeedEntry entry = new FeedEntry(title,
                                            new FeaturePageUrl(feature, FeatureTabKey.description).externalUrlString(),
                                            new FeaturePageUrl(feature, FeatureTabKey.description).externalUrlString(),
                                            feature.getCreationDate(),
                                            translationText,
                                            feature.getMember().getDisplayName(),
                                            new MemberPageUrl(feature.getMember()).externalUrlString());
            addFeedEntry(entry, Position.LAST);
            if (first) {
                updateDate = feature.getCreationDate();
            }
        }
    }

    @Override
    public String getFeedTitle() {
        return Context.tr("Elveos feature feed for software {0}.", software.getName());
    }

    @Override
    public Date getUpdatedDate() {
        if (updateDate != null) {
            return updateDate;
        }
        return new Date();
    }
}
