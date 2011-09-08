package com.bloatit.web.linkable.atom;

import java.util.Date;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.linkable.atom.master.ElveosAtomFeed;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.url.FeaturePageUrl;

/**
 * A feed used to display the recent features in the elveos website
 */
@ParamContainer("featurefeed")
public class FeatureAtomFeed extends ElveosAtomFeed {
    private Date updateDate;
    
    public FeatureAtomFeed(Url url) {
        super(url);
    }

    @Override
    public void generate() {
        boolean first = true;
        for (Feature feature : FeatureManager.getAllByCreationDate()) {
            FeedEntry entry = new FeedEntry(feature.getTitle(),
                                            new FeaturePageUrl(feature, FeatureTabKey.description).externalUrlString(),
                                            "featurefeed-" + feature.getId(),
                                            feature.getCreationDate(),
                                            feature.getDescription().getDefaultTranslation().getText());
            addFeedEntry(entry, Position.LAST);
            if(first){
                updateDate = feature.getCreationDate();
            }
        }
    }

    @Override
    public String getFeedTitle() {
        return "Elveos features feed";
    }

    @Override
    public Date getUpdatedDate() {
        if(updateDate != null){
            return updateDate;
        }
        return new Date();
    }
}
