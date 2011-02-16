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

import com.bloatit.data.DaoJoinGroupInvitation;

/**
 * This is an invitation to join a group. Some groups are not public, and you have to have
 * an invitation to join it.
 * 
 * @author Thomas Guyard
 */
public final class JoinGroupInvitation extends Identifiable<DaoJoinGroupInvitation> {

    private JoinGroupInvitation(final DaoJoinGroupInvitation dao) {
        super(dao);
    }

    public static JoinGroupInvitation create(final DaoJoinGroupInvitation dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoJoinGroupInvitation> created = CacheManager.get(dao);
            if (created == null) {
                return new JoinGroupInvitation(dao);
            }
            return (JoinGroupInvitation) created;
        }
        return null;
    }

    public Member getSender() {
        return Member.create(getDao().getSender());
    }

    public Member getReciever() {
        return Member.create(getDao().getReceiver());
    }

    public Group getGroup() {
        return Group.create(getDao().getGroup());
    }

    /**
     * @see DaoJoinGroupInvitation#accept()
     */
    protected void accept() {
        getDao().accept();
    }

    /**
     * @see DaoJoinGroupInvitation#refuse()
     */
    protected void refuse() {
        getDao().refuse();
    }
    
    /**
     * @see DaoJoinGroupInvitation#discard()
     */
    protected void discard(){
        getDao().discard();
    }

}
