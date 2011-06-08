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

import com.bloatit.data.DaoContact;

/**
 * This is a invoicing contact.
 */
public final class Contact {

    private final DaoContact dao;

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////



    /**
     * Find a bug in the cache or create an new one.
     *
     * @param dao the dao
     * @return null if dao is null. Else return the new invoicing contact.
     */
    @SuppressWarnings("synthetic-access")
    public static Contact create(final DaoContact dao) {
        if(dao == null) {
            return null;
        }
        return new Contact(dao);
    }

    /**
     * Instantiates a new invoicing contact.
     *
     * @param dao the dao
     */
    private Contact(final DaoContact dao) {
        this.dao = dao;
    }


    public void setName(String name) {
        getDao().setName(name);
    }

    public void setAddress(String address) {
        getDao().setAddress(address);
    }


    // ///////////////////////////
    // Unprotected methods

    /**
     * This method is used only in the authentication process. You should never
     * used it anywhere else.
     *
     * @return the actor unprotected
     * @see #getActor()
     */
//    final Actor<?> getActorUnprotected() {
//        return (Actor<?>) getDao().getActor().accept(new DataVisitorConstructor());
//    }

    private DaoContact getDao() {
        return dao;
    }

    public String getName() {
        //TODO: access right
        return getDao().getName();
    }

    public String getAddress() {
        //TODO: access right
        return getDao().getAddress();
    }



}
