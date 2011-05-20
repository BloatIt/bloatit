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

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoInvoicingContact;

/**
 * This is a invoicing contact.
 */
public final class InvoicingContact extends Identifiable<DaoInvoicingContact> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoInvoicingContact, InvoicingContact> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public InvoicingContact doCreate(final DaoInvoicingContact dao) {
            return new InvoicingContact(dao);
        }
    }

    /**
     * Find a bug in the cache or create an new one.
     *
     * @param dao the dao
     * @return null if dao is null. Else return the new invoicing contact.
     */
    @SuppressWarnings("synthetic-access")
    public static InvoicingContact create(final DaoInvoicingContact dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new invoicing contact.
     *
     * @param dao the dao
     */
    private InvoicingContact(final DaoInvoicingContact dao) {
        super(dao);
    }

    /**
     * Create a new invoicing contact.
     *
     * @param member is the author of the bug.
     * @param milestone is the milestone on which this bug has been set.
     * @param title is the title of the bug.
     * @param description is a complete description of the bug.
     * @param locale is the language in which this description has been written.
     * @param errorLevel is the estimated level of the bug. see {@link Level}.
     */
    InvoicingContact(final String name,
                     final String address, final Actor<?> actor) {
        super(DaoInvoicingContact.createAndPersist(name, address, actor.getDao()));
        }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
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
    final Actor<?> getActorUnprotected() {
        return (Actor<?>) getDao().getActor().accept(new DataVisitorConstructor());
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

}
