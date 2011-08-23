package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoFollow;
import com.bloatit.data.DaoFollow.FollowState;
import com.bloatit.data.DaoUserContent;
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

    protected Follow(DaoFollow dao) {
        super(dao);
    }

    /**
     * Make the user <code>follower</code> follow the content
     * <code>toFollow</code>
     * 
     * @param toFollow the content to follow
     * @param follower the user than will follow the content
     */
    public Follow(UserContent<? extends DaoUserContent> toFollow, Actor<? extends DaoActor> follower) {
        super(DaoFollow.createAndPersist(follower.getDao(), toFollow.getDao()));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the state of the follow
     */
    public FollowState getFollowState() {
        return getDao().getFollowState();
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
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(ModelClassVisitor<ReturnType> visitor) {
        return null;
    }
}
