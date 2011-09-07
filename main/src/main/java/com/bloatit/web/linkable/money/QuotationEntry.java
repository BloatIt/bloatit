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
package com.bloatit.web.linkable.money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;

public abstract class QuotationEntry {
    private boolean closed;
    protected final List<QuotationEntry> entries = new ArrayList<QuotationEntry>();

    protected QuotationEntry() {
        super();
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(final boolean closed) {
        this.closed = closed;
    }

    public abstract BigDecimal getValue();

    public QuotationEntry addEntry(final QuotationEntry entry) {
        entries.add(entry);
        return this;
    }

    public List<QuotationEntry> getChildren() {
        return entries;
    }

    public abstract void accept(QuotationVisitor visitor);

}
