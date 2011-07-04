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

import org.hibernate.Query;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoMember;
import com.bloatit.data.SessionManager;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Member;
import com.bloatit.model.lists.MemberList;

/**
 * The Class MemberManager is an utility class containing static methods.
 */
public final class MemberManager {

    /**
     * Desactivated constructor on utility class.
     */
    private MemberManager() {
        // Desactivate default ctor
    }

    /**
     * Gets the member by login.
     *
     * @param login the login
     * @return the member or <code>null</code> if not found.
     */
    public static Member getMemberByLogin(final String login) {
        final DaoMember daoMember = DaoMember.getByLogin(login);
        if (daoMember == null) {
            return null;
        }

        return Member.create(daoMember);
    }

    public static Member getMemberByEmail(final String email) {
        final DaoMember daoMember = DaoMember.getByEmail(email);
        if(daoMember == null){
            return null;
        }
        return Member.create(daoMember);
    }

    /**
     * Tells if a Login exists.
     *
     * @param login the login
     * @return <code>true</code>, if it exists, <code>false</code> otherwise.
     */
    public static boolean loginExists(final String login) {
        return DaoActor.loginExists(login);
    }

    /**
     * Tells if Email exists.
     *
     * @param email the email
     * @return <code>true</code>, if it exists, <code>false</code> otherwise.
     */
    public static boolean emailExists(final String email) {
        return DaoActor.emailExists(email);
    }

    /**
     * Gets the member by id.
     *
     * @param id the id
     * @return the member or <code>null</code> if not found.
     */
    public static Member getById(final Integer id) {
        return Member.create(DBRequests.getById(DaoMember.class, id));
    }

    /**
     * Gets all the members.
     *
     * @return the all the members in the DB.
     */
    public static PageIterable<Member> getAll() {
        return new MemberList(DBRequests.getAll(DaoMember.class));
    }
    
    
    /**
     * Gets all the members ordered by name.
     *
     * @return the all the members in the DB.
     */
    public static PageIterable<Member> getAllMembersOrderByName() {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(DaoMember.class);
        final Query query = SessionManager.createQuery("FROM " + meta.getEntityName() + " ORDER BY coalesce(fullname,login) ASC");
        final Query size = SessionManager.createQuery("SELECT count(*) FROM " + meta.getEntityName());
        return new MemberList(new QueryCollection<DaoMember>(query, size));
    }
    

    /**
     * Gets the number of members.
     *
     * @return the members count
     */
    public static int getMembersCount() {
        return DBRequests.count(DaoMember.class);
    }

}
