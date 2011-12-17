package com.bloatit.web.linkable;

import java.util.Date;

import com.bloatit.framework.webprocessor.masters.AtomFeed;
import com.bloatit.framework.webprocessor.masters.HtmlHeaderLink;
import com.bloatit.framework.webprocessor.url.Url;

public abstract class ElveosAtomFeed extends AtomFeed {
    private final Url privateUrl;

    protected ElveosAtomFeed(final Url url) {
        super();
        this.privateUrl = url;
        generate();
    }

    @Override
    public abstract void generate();

    @Override
    public abstract String getFeedTitle();

    @Override
    public String getFeedSubtitle() {
        return null;
    }

    @Override
    public String getLink() {
        return privateUrl.externalUrlString();
    }

    @Override
    public abstract Date getUpdatedDate();

    @Override
    public String getAuthorName() {
        return "Elveos";
    }

    @Override
    public String getAuthorEmail() {
        return "contact@elveos.org";
    }

    @Override
    public final String getId() {
        return "https://elveos.org/";
    }

    public static HtmlHeaderLink generateHeaderLink(final Url url, final String feedTitle) {
        return new HtmlHeaderLink(url.externalUrlString(), "application/atom+xml", "alternate", feedTitle);
    }
}
