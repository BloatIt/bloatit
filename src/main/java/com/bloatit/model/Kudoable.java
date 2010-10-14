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

/**
 * Interface that describes any elements that can be kudoed
 */
public interface Kudoable {

        /**
     * Call when a member kudos a kudoable
     * @param kudoer the member that issues the kudo
     */
    public void kudo(Member kudoer);

    /**
     * Call when a member reports a kudoable
     * @param kudoer the member that issues the report
     */
    public void report(Member reporter);

    /**
     * Returns the current reputation of the kudoable
     * @return
     */
    public long getReputation();

    /**
     * Returns the author of the kudoable
     * @return
     */
    public Member getAuthor();
}