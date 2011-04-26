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

import com.bloatit.data.DaoJoinTeamInvitation;

/**
 * This is an invitation to join a team. Some teams are not public, and you have
 * to have an invitation to join it.
 * 
 * @author Thomas Guyard
 */
public final class JoinTeamInvitation extends Identifiable<DaoJoinTeamInvitation> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoJoinTeamInvitation, JoinTeamInvitation> {
        @SuppressWarnings("synthetic-access")
        @Override
        public JoinTeamInvitation doCreate(final DaoJoinTeamInvitation dao) {
            return new JoinTeamInvitation(dao);
        }
    }

    private JoinTeamInvitation(final DaoJoinTeamInvitation dao) {
        super(dao);
    }

    @SuppressWarnings("synthetic-access")
    public static JoinTeamInvitation create(final DaoJoinTeamInvitation dao) {
        return new MyCreator().create(dao);
    }

    public Member getSender() {
        return Member.create(getDao().getSender());
    }

    public Member getReciever() {
        return Member.create(getDao().getReceiver());
    }

    public Team getTeam() {
        return Team.create(getDao().getTeam());
    }

    /**
     * @return <i>true</i> if accepted, <i>false</i> otherwise.
     * @see DaoJoinTeamInvitation#accept()
     */
    protected boolean accept() {
        return getDao().accept();
    }

    /**
     * @see DaoJoinTeamInvitation#refuse()
     */
    protected void refuse() {
        getDao().refuse();
    }

    /**
     * @see DaoJoinTeamInvitation#discard()
     */
    protected void discard() {
        getDao().discard();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
