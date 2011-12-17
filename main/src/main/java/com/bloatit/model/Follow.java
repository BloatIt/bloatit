package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoFollow;
import com.bloatit.data.DaoFollow.FollowState;
import com.bloatit.model.visitor.ModelClassVisitor;

public class Follow extends Identifiable<DaoFollow> {

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoFollow, Follow> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Follow doCreate(final DaoFollow dao) {
            return new Follow(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Follow create(final DaoFollow dao) {
        return new MyCreator().create(dao);
    }

    protected Follow(final DaoFollow dao) {
        super(dao);
    }

    /**
     * Make the user <code>follower</code> follow the content
     * <code>toFollow</code>. <br />
     * If the feature is already followed by the same actor, it is not followed
     * again, but no error is thrown (trying to follow even when you already do,
     * is the normal behavior).
     * 
     * @param toFollow the content to follow
     * @param follower the user than will follow the content
     */
    public Follow(final FeatureImplementation toFollow, final Actor<? extends DaoActor> follower) {
        super(DaoFollow.createAndPersist(follower.getDao(), toFollow.getDao()));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public void delete() {
        getDao().delete();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the state of the follow
     */
    public FollowState getFollowState() {
        return getDao().getFollowState();
    }

    public Feature getFollowed() {
        return FeatureImplementation.create(getDao().getFollowed());
    }

    public Actor<?> getActor() {
        return Actor.getActorFromDao(getDao().getActor());
    }

    public Date getCreationDate() {
        return getDao().getCreationDate();
    }

    public Date getLastConsultationDate() {
        return getDao().getLastConsultationDate();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Static getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public static Follow getFollow(final FeatureImplementation content, final Actor<? extends DaoActor> follower) {
        return new Follow(DaoFollow.getFollow(content.getDao(), follower.getDao()));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
