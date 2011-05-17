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

import org.apache.commons.lang.NotImplementedException;

public class AbstractModelClassVisitor<T> implements ModelClassVisitor<T> {

    @Override
    public T visit(final ExternalAccount model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final InternalAccount model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Member model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final BankTransaction model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Milestone model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Description model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Team model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final HighlightFeature model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final JoinTeamInvitation model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Software model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Transaction model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Bug model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Contribution model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final FileMetadata model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Kudos model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Comment model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Feature model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Offer model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Translation model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(final Release model) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(MoneyWithdrawal moneyWithdrawal) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(Invoice invoice) {
        throw new NotImplementedException();
    }

    @Override
    public T visit(InvoicingContact invoicingContact) {
        throw new NotImplementedException();
    }
}
