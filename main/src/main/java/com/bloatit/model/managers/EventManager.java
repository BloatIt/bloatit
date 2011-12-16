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
package com.bloatit.model.managers;

import java.util.Date;
import java.util.Iterator;

import com.bloatit.data.DaoEvent;
import com.bloatit.data.DaoMember.EmailStrategy;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Event;
import com.bloatit.model.Member;
import com.bloatit.model.lists.ListBinder;

public final class EventManager {

    private EventManager() {
        // Desactivate default ctor
    }

    public static EventList getAllEventAfter(Date beginDate, Date endDate,  EmailStrategy strategy) {
        QueryCollection<Object[]> q = new QueryCollection<Object[]>("memberid.event.byDate.withMail");
        q.setTimestamp("beginDate", beginDate);
        q.setTimestamp("endDate", endDate);
        q.setParameter("strategy", strategy);
        return new EventList(q, -1);
    }
    
    public static PageIterable<Event> getAllEvents() {
        return new ListBinder<Event, DaoEvent>(new QueryCollection<DaoEvent>("event.getall"));
    }
    
    public static PageIterable<Event> getAllEventByMember(Member member) {
        return new ListBinder<Event, DaoEvent>(new QueryCollection<DaoEvent>("event.byMember").setEntity("member", member.getDao()));
    }

    public static class EventList {
        private Iterator<Object[]> it;
        private Member currentMember;
        private Event currentEvent;
        private final PageIterable<Object[]> list;

        protected EventList(PageIterable<Object[]> list, int maxSize) {
            super();
            this.list = list;
            if(maxSize > 0) {
                this.list.setPageSize(maxSize);
            }
            reset();
        }

        public boolean hasNext() {
            return it.hasNext();
        }
        
        public void reset() {
            it = list.iterator();
        }

        public Member member() {
            return currentMember;
        }

        public Event event() {
            return currentEvent;
        }

        public void next() {
            Object[] next = it.next();
            if (currentMember == null || !currentMember.getId().equals(next[0])) {
                currentMember = MemberManager.getById((Integer) next[0]);
            }
            currentEvent = Event.create((DaoEvent) next[1]);
        }
    }
}
