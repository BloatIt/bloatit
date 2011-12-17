package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.components.meta.XmlElement;
import com.bloatit.framework.xcgiserver.HttpResponse;

/**
 * The root class to create atom feeds
 */
public abstract class AtomFeed implements Linkable {
    private final ArrayDeque<FeedEntry> entries;

    public enum Position {
        FIRST, LAST
    }

    protected AtomFeed() {
        this.entries = new ArrayDeque<AtomFeed.FeedEntry>();
    }

    @Override
    public void writeToHttp(HttpResponse response, WebProcessor webServer) throws RedirectException, IOException {
        generate();
        response.writeAtomFeed(this);
    }
    
    public abstract void generate();

    public abstract String getFeedTitle();

    public abstract String getFeedSubtitle();

    public abstract String getLink();

    public abstract Date getUpdatedDate();

    public abstract String getAuthorName();

    public abstract String getAuthorEmail();

    public abstract String getId();

    /**
     * Adds a new entry to the feed
     */
    public void addFeedEntry(FeedEntry feedentry, Position position) {
        switch (position) {
            case FIRST:
                entries.addFirst(feedentry);
                break;
            case LAST:
                entries.addLast(feedentry);
                break;
        }
    }

    /**
     * Writes the content of the feed
     * 
     * @param output The stream to output the content of the feed
     * @throws IOException when an error occurs when writing into the stream
     */
    public final void write(OutputStream output) throws IOException {
        AtomSerializer serializer = new AtomSerializer(this);
        output.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>".getBytes());

        XmlElement feed = new XmlElement("feed").addAttribute("xmlns", "http://www.w3.org/2005/Atom");
        serializer.addFeedHeader(feed);
        serializer.addFeedBody(feed);

        feed.write(output);
    }

    /**
     * Describes an entry of the feed
     */
    public class FeedEntry {
        private final String author;
        private final String authorUri;
        private final String title;
        private final String link;
        private final String id;
        private final Date updated;
        private final String content;

        public FeedEntry(String title, String link, String id, Date updated, String content, String author, String authorUri) {
            super();
            this.title = title;
            this.link = link;
            this.id = id;
            this.updated = updated;
            this.content = content;
            this.author = author;
            this.authorUri = authorUri;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getId() {
            return id;
        }

        public Date getUpdated() {
            return updated;
        }

        public String getContent() {
            return content;
        }

        public String getAuthor() {
            return author;
        }

        public String getAuthorUri() {
            return authorUri;
        }
    }

    /**
     * Serialize an atom feed into a string
     */
    private class AtomSerializer {
        private final AtomFeed feed;

        public AtomSerializer(AtomFeed feed) {
            this.feed = feed;
        }

        private String formatRFC3339(Date date) {
            return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(date);
        }

        public void addFeedHeader(XmlElement all) {
            all.add(new XmlElement("title").addText(feed.getFeedTitle()));

            if (getFeedSubtitle() != null) {
                all.add(new XmlElement("subtitle").addText(feed.getFeedSubtitle()));
            }

            if (getLink() != null) {
                final XmlElement link = new XmlElement("link");
                all.add(link);
                link.addAttribute("href", feed.getLink());
                link.addAttribute("rel", "self");
                link.addAttribute("type", "application/rss+xml");
            }

            all.add(new XmlElement("updated").addText(formatRFC3339(feed.getUpdatedDate())));

            final XmlElement author = new XmlElement("author");
            all.add(author);
            author.add(new XmlElement("name").addText(feed.getAuthorName()));
            author.add(new XmlElement("email").addText(feed.getAuthorEmail()));

            if (getId() != null) {
                all.add(new XmlElement("id").addText(feed.getId()));
            }
        }

        public void addFeedBody(XmlElement all) {
            for (FeedEntry entry : feed.entries) {
                final XmlElement xmlEntry = new XmlElement("entry");
                all.add(xmlEntry);

                xmlEntry.add(new XmlElement("title").addText(entry.getTitle()));
                xmlEntry.add(new XmlElement("link").addAttribute("href", entry.getLink()));
                xmlEntry.add(new XmlElement("id").addText(entry.getId()));
                xmlEntry.add(new XmlElement("updated").addText(formatRFC3339(entry.getUpdated())));
                final XmlElement author = new XmlElement("author");
                xmlEntry.add(author);
                author.add(new XmlElement("name").addText(entry.getAuthor()));
                author.add(new XmlElement("uri").addText(entry.getAuthorUri()));
                xmlEntry.add(new XmlElement("content").addAttribute("type", "html").addText(entry.content));
            }
        }
    }
}
