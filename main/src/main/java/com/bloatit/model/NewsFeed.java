package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoNewsFeed;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;

/**
 * News feed are used to display some updated to the users of the website
 */
public class NewsFeed extends Identifiable<DaoNewsFeed> {
    private static final int MAX_MESSAGE_LENGTH = 140;

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoNewsFeed, NewsFeed> {
        @SuppressWarnings("synthetic-access")
        @Override
        public NewsFeed doCreate(final DaoNewsFeed dao) {
            return new NewsFeed(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static NewsFeed create(final DaoNewsFeed dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Creates an entry in the news feed with <code>message</code> as the
     * content
     * 
     * @param message
     */
    public NewsFeed(String message) {
        super(DaoNewsFeed.createAndPersist(message));
        if (message == null) {
            throw new NonOptionalParameterException("Message content is not optional");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new BadProgrammerException("Message must be less than " + MAX_MESSAGE_LENGTH + " characters");
        }
        FrameworkConfiguration.getMicroBlogs().post(message);
    }

    protected NewsFeed(DaoNewsFeed dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return The content of the news feed item
     */
    public String getMessage() {
        return getDao().getMessage();
    }

    /**
     * @return The creation date of the news feed.
     */
    public Date getCreationDate() {
        return getDao().getCreationDate();
    }

    public boolean isDeleted() {
        return getDao().isDeleted();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public void delete() {
        getDao().delete();
    }

    public void restore() {
        getDao().restore();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
