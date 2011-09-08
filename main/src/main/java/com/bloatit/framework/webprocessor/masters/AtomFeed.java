package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.WebProcessor;
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
        response.writeAtomFeed(this);
    }

    public abstract String getFeedTitle();

    public abstract String getFeedSubtitle();

    public abstract String getLink();

    public abstract Date getUpdatedDate();

    public abstract String getAuthorName();

    public abstract String getAuthorEmail();

    public abstract String getId();

    /**
     * Adds a new entry to the feed
     * 
     * @param title The title of the feed entry
     * @param link The link to the content of the feed entry
     * @param id A unique id for the feed entry
     * @param updated The update date for the feed entry
     * @param summary The summary of the feed entry
     * @param position The position <i>FIRST</i> or <i>LAST</i> in the feed
     */
    public void addFeedEntry(String title, String link, String id, Date updated, String summary, Position position) {
        addFeedEntry(new FeedEntry(title, link, id, updated, summary), position);
    }

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
        output.write("<feed xmlns=\"http://www.w3.org/2005/Atom\">".getBytes());
        output.write(serializer.getFeedHeader().getBytes());
        output.write(serializer.getFeedBody().getBytes());
        output.write("</feed>".getBytes());
    }

    /**
     * Describes an entry of the feed
     */
    public class FeedEntry {
        private final String title;
        private final String link;
        private final String id;
        private final Date updated;
        private final String summary;

        public FeedEntry(String title, String link, String id, Date updated, String summary) {
            super();
            this.title = title;
            this.link = link;
            this.id = id;
            this.updated = updated;
            this.summary = summary;
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

        public String getSummary() {
            return summary;
        }
    }

    /**
     * Serialize an atom feed into a string
     */
    private class AtomSerializer {
        DateFormat ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        private final AtomFeed feed;

        public AtomSerializer(AtomFeed feed) {
            this.feed = feed;
        }

        public String getFeedHeader() {
            StringBuilder sb = new StringBuilder();
            sb.append("<title>");
            sb.append(feed.getFeedTitle());
            sb.append("</title>");

            if (getFeedSubtitle() != null) {
                sb.append("<subtitle>");
                sb.append(feed.getFeedSubtitle());
                sb.append("</subtitle>");
            }

            if (getLink() != null) {
                sb.append("<link>");
                sb.append(feed.getLink());
                sb.append("</link>");
            }

            sb.append("<updated>");
            sb.append(ISO8601Local.format(feed.getUpdatedDate()));
            sb.append("</updated>");
            sb.append("<author>");
            {
                sb.append("<name>");
                sb.append(feed.getAuthorName());
                sb.append("</name>");
                sb.append("<email>");
                sb.append(feed.getAuthorEmail());
                sb.append("</email>");
            }
            sb.append("</author>");

            if (getId() != null) {
                sb.append(feed.getId());
                sb.append("</id>");
            }

            return sb.toString();
        }

        public String getFeedBody() {
            StringBuilder sb = new StringBuilder();

            for (FeedEntry entry : feed.entries) {
                sb.append("<entry>");
                {
                    sb.append("<title>");
                    sb.append(entry.getTitle());
                    sb.append("</title>");
                    sb.append("<link href=\"");
                    sb.append(entry.getLink());
                    sb.append("\" />");
                    sb.append("<id>");
                    sb.append(entry.getId());
                    sb.append("</id>");
                    sb.append("<updated>");
                    sb.append(ISO8601Local.format(entry.getUpdated()));
                    sb.append("</updated>");
                    sb.append("<summary>");
                    sb.append(entry.summary);
                    sb.append("</summary>");
                }
                sb.append("</entry>");
            }

            return sb.toString();
        }
    }
}
