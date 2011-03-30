package com.bloatit.web.linkable.money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.web.linkable.money.Quotation.QuotationVisitor;

public abstract class QuotationEntry {
    private final String label;
    private final String comment;
    private boolean closed;
    protected final List<QuotationEntry> entries = new ArrayList<QuotationEntry>();

    public QuotationEntry(String label, String comment) {
        super();
        this.label = label;
        this.comment = comment;
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public abstract BigDecimal getValue();

    public String getLabel() {
        return label;
    }

    public String getComment() {
        return comment;
    }

    public QuotationEntry addEntry(QuotationEntry entry) {
        entries.add(entry);
        return this;
    }

    public List<QuotationEntry> getChildren() {
        return entries;
    }

    public abstract void accept(QuotationVisitor visitor);

}