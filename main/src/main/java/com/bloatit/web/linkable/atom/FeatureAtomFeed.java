package com.bloatit.web.linkable.atom;

import java.util.Date;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.cache.MemoryCache;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.utils.parsers.MarkdownParser;
import com.bloatit.framework.utils.parsers.ParsingException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Software;
import com.bloatit.model.Translation;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.linkable.atom.master.ElveosAtomFeed;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.MemberPageUrl;

/**
 * A feed used to display the recent features in the elveos website
 */
@ParamContainer("featurefeed")
public class FeatureAtomFeed extends ElveosAtomFeed {
    private Date updateDate;

    public FeatureAtomFeed(final Url url) {
        super(url);
    }

    @Override
    public void generate() {
        boolean first = true;
        for (final Feature feature : FeatureManager.getAllByCreationDate()) {
            final Translation translation = feature.getDescription()
                                                   .getTranslationOrDefault(Language.fromLocale(Context.getLocalizator().getLocale()));
            final String featureTitle = translation.getTitle();
            final Software software = feature.getSoftware();
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

            final FeedEntry entry = new FeedEntry(title,
                                                  new FeaturePageUrl(feature, FeatureTabKey.description).externalUrlString(),
                                                  new FeaturePageUrl(feature, FeatureTabKey.description).externalUrlString(),
                                                  feature.getCreationDate(),
                                                  translationText,
                                                  feature.getMember().getDisplayName(),
                                                  new MemberPageUrl(feature.getMember()).externalUrlString());
            addFeedEntry(entry, Position.LAST);
            if (first) {
                updateDate = feature.getCreationDate();
                first = false;
            }
        }
    }

    @Override
    public String getFeedTitle() {
        return "Elveos features feed";
    }

    @Override
    public Date getUpdatedDate() {
        if (updateDate != null) {
            return updateDate;
        }
        return new Date();
    }
}
