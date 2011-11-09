package com.bloatit.web.linkable.atom.master;

import java.util.Date;

import com.bloatit.framework.webprocessor.masters.AtomFeed;
import com.bloatit.framework.webprocessor.masters.HtmlHeaderLink;
import com.bloatit.framework.webprocessor.url.Url;

public abstract class ElveosAtomFeed extends AtomFeed {
    private final Url privateUrl;

    protected ElveosAtomFeed(Url url) {
        super();
        this.privateUrl = url;
    }

    public abstract String getFeedTitle();

    public String getFeedSubtitle() {
        return null;
    }

    public String getLink() {
        return privateUrl.externalUrlString();
    }

    public abstract Date getUpdatedDate();

    public String getAuthorName() {
        return "Elveos";
    }

    public String getAuthorEmail() {
        return "contact@elveos.org";
    }

    public final String getId() {
        return "https://elveos.org/";
    }

    public static HtmlHeaderLink generateHeaderLink(Url url, String feedTitle) {
        return new HtmlHeaderLink(url.externalUrlString(), "application/atom+xml", "alternate", feedTitle);
    }
}
