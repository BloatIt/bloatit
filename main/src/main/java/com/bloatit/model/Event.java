//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import java.util.Date;

import com.bloatit.data.DaoBug;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoEvent;
import com.bloatit.data.DaoEvent.EventType;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoRelease;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class Event extends Identifiable<DaoEvent> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoEvent, Event> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Event doCreate(final DaoEvent dao) {
            return new Event(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Event create(final DaoEvent dao) {
        return new MyCreator().create(dao);
    }

    private Event(final DaoEvent id) {
        super(id);
    }

    public FeatureEvent getEvent() {
        switch (getDao().getType()) {
            case CREATE_FEATURE:
            case IN_DEVELOPPING_STATE:
            case DISCARDED:
            case FINICHED:
                return new FeatureEvent(getDao());

            case ADD_CONTRIBUTION:
            case REMOVE_CONTRIBUTION:
                return new ContributionEvent(getDao());

            case ADD_OFFER:
            case REMOVE_OFFER:
            case ADD_SELECTED_OFFER:
            case CHANGE_SELECTED_OFFER:
            case REMOVE_SELECTED_OFFER:
                return new OfferEvent(getDao());

            case ADD_RELEASE:
                return new ReleaseEvent(getDao());

            case ADD_BUG:
            case BUG_CHANGE_LEVEL:
            case BUG_SET_RESOLVED:
            case BUG_SET_DEVELOPING:
                return new BugEvent(getDao());

            case FEATURE_ADD_COMMENT:
                return new FeatureCommentEvent(getDao());

            case RELEASE_ADD_COMMENT:
                return new ReleaseCommentEvent(getDao());

            case BUG_ADD_COMMENT:
                return new BugCommentEvent(getDao());

            default:
                throw new BadProgrammerException("You should have covered all cases. Default should never happen.");

        }
    }

    public static interface EventVisitor<ReturnType> {
        ReturnType visit(FeatureEvent event);

        ReturnType visit(BugEvent event);

        ReturnType visit(BugCommentEvent event);

        ReturnType visit(ContributionEvent event);

        ReturnType visit(FeatureCommentEvent event);

        ReturnType visit(OfferEvent event);

        ReturnType visit(ReleaseEvent event);

        ReturnType visit(ReleaseCommentEvent event);
    }

    public static class FeatureEvent {
        protected final DaoEvent event;

        public FeatureEvent(DaoEvent event) {
            this.event = event;
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }

        public EventType getType() {
            return event.getType();
        }

        public DaoFeature getFeature() {
            return event.getFeature();
        }

        public Date getCreationDate() {
            return event.getCreationDate();
        }
    }

    public static class ContributionEvent extends FeatureEvent {
        public ContributionEvent(DaoEvent event) {
            super(event);
        }

        public DaoContribution getContribution() {
            return event.getContribution();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class OfferEvent extends FeatureEvent {
        public OfferEvent(DaoEvent event) {
            super(event);
        }

        public DaoOffer getOffer() {
            return event.getOffer();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class FeatureCommentEvent extends FeatureEvent {
        public FeatureCommentEvent(DaoEvent event) {
            super(event);
        }

        public DaoComment getComment() {
            return event.getComment();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class ReleaseEvent extends FeatureEvent {
        public ReleaseEvent(DaoEvent event) {
            super(event);
        }

        public DaoRelease getRelease() {
            return event.getRelease();
        }

        public DaoOffer getOffer() {
            return event.getOffer();
        }

        public DaoMilestone getMilestone() {
            return event.getMilestone();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class ReleaseCommentEvent extends ReleaseEvent {

        public ReleaseCommentEvent(DaoEvent event) {
            super(event);
        }

        public DaoComment getComment() {
            return event.getComment();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class BugEvent extends FeatureEvent {

        public BugEvent(DaoEvent event) {
            super(event);
        }

        public DaoBug getBug() {
            return event.getBug();
        }

        public DaoOffer getOffer() {
            return event.getOffer();
        }

        public DaoMilestone getMilestone() {
            return event.getMilestone();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    public static class BugCommentEvent extends BugEvent {

        public BugCommentEvent(DaoEvent event) {
            super(event);
        }

        public DaoComment getComment() {
            return event.getComment();
        }

        public <ReturnType> ReturnType accept(EventVisitor<ReturnType> visitor) {
            return visitor.visit(this);
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
