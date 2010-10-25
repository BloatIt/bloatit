/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.model;

public class Member extends  Unlockable {

    private int id;
    private String login;
    private String email;
    private String fullName;
    private long karma;

    public Member(int id, String login, String email, String fullName, long karma) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.fullName = fullName;
        this.karma = karma;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public int getId() {
        return id;
    }

    public long getKarma() {
        return karma;
    }

    /**
     * Call when contribution from a member is kudoed by another member.
     * Only called from package. Use Kudoable.kudo instead
     * @param kudoer the person that issues the kudo
     */
    void receiveKudo(Member kudoer) {
        this.karma += (long)(Math.log(kudoer.getKarma())/Math.log(2));
    }

    /**
     * Call when a user issues a report.
     * Only called from package. Use Kudoable.kudo instead
     */
    void issueKudo() {
        // Nothing
    }

    /**
     * Call when a user issues a report. 
     * Only called from package. Use Kudoable.report instead
     */
    void issueReport() {
        this.karma -= 1;
    }

    /**
     * Call when a member contribution is reported.
     * Only called from package. Use Kudoable.report instead
     * @param reporter
     */
    void receiveReport(Member reporter) {
        this.karma += (long)(Math.log(reporter.getKarma())/Math.log(2));
    }
}