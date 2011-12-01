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

public final class EventManager {

    private EventManager() {
        // Desactivate default ctor
    }

    public static EventList getAllEventAfter(Date date, EmailStrategy strategy) {
        QueryCollection<Object[]> q = new QueryCollection<Object[]>("memberid.event.byDate.withMail");
        q.setTimestamp("date", date);
        q.setParameter("strategy", strategy);
        return new EventList(q);
    }
    
    public static EventList getAllEventByMemberAfter(Date date, Member member) {
        QueryCollection<Object[]> q = new QueryCollection<Object[]>("event.byDate.byMember");
        q.setTimestamp("date", date);
        q.setEntity("member", member.getDao());
        return new EventList(q);
    }

    public static class EventList {
        private Iterator<Object[]> it;
        private Member currentMember;
        private Event currentEvent;

        protected EventList(PageIterable<Object[]> list) {
            super();
            it = list.iterator();
        }

        public boolean hasNext() {
            return it.hasNext();
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
