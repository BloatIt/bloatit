package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.components.meta.XmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.xcgiserver.AvailableLocales;
import com.bloatit.framework.xcgiserver.HttpResponse;

/**
 * The root class to create atom feeds
 */
public abstract class SiteMap implements Linkable {
    private final ArrayDeque<SiteMapEntry> entries;

    public enum ChangeFrequency {
        ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER
    }

    protected SiteMap() {
        this.entries = new ArrayDeque<SiteMapEntry>();
    }

    @Override
    public void writeToHttp(HttpResponse response, WebProcessor webServer) throws RedirectException, IOException {
        response.writeSiteMap(this);
    }

    /**
     * Adds a new entry to the feed
     */
    public void addSiteMapEntry(SiteMapEntry entry) {
        entries.add(entry);
    }

    /**
     * Writes the content of the feed
     *
     * @param output The stream to output the content of the feed
     * @throws IOException when an error occurs when writing into the stream
     */
    public final void write(OutputStream output) throws IOException {
        output.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>".getBytes());

        if (entries.size() > 10000) {
            Log.framework().error("WARNING: the sitemap has " + entries.size() + " entries !");
        }

        // TODO: optimize for big website

        XmlElement siteMap = new XmlElement("urlset").addAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

        Map<String, String> availableLanguages = AvailableLocales.getAvailableLangs();
        for (SiteMapEntry entry : entries) {
            for (String locale : availableLanguages.keySet()) {
                siteMap.add(entry.generate(new Locale(locale)));
            }
        }

        siteMap.write(output);

    }

    /**
     * Describes an entry of the feed
     */
    public class SiteMapEntry {
        private final Url url;
        private final ChangeFrequency changeFrequency;
        private final Date lastModified;
        private final float priority;

        public SiteMapEntry(Url url, ChangeFrequency changeFrequency, Date lastModified, float priority) {
            this.url = url;
            this.changeFrequency = changeFrequency;
            this.lastModified = lastModified;
            this.priority = priority;

        }

        public XmlNode generate(Locale language) {
            XmlElement siteMapUrl = new XmlElement("url");

            XmlElement locTag = new XmlElement("loc");
            locTag.addText(url.externalUrlString(language));
            siteMapUrl.add(locTag);

            XmlElement changeFreqTag = new XmlElement("changefreq");
            changeFreqTag.addText(changeFrequency.toString().toLowerCase());
            siteMapUrl.add(changeFreqTag);

            XmlElement priorityTag = new XmlElement("priority");
            priorityTag.addText(String.valueOf(priority));
            siteMapUrl.add(priorityTag);

            if (lastModified != null) {
                XmlElement lastMod = new XmlElement("lastmod");
                lastMod.addText(new SimpleDateFormat("yyyy-MM-dd").format(lastModified));
                siteMapUrl.add(lastMod);
            }

            return siteMapUrl;
        }

    }

}
