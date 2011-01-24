package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.model.data.DaoBatch;

public class Batch extends Identifiable {

    private final DaoBatch dao;

    public static Batch create(final DaoBatch dao) {
        if (dao != null) {
            return new Batch(dao);
        }
        return null;
    }

    private Batch(final DaoBatch dao) {
        this.dao = dao;
    }

    @Override
    public int getId() {
        return getDao().getId();
    }

    public Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    public BigDecimal getAmount() {
        return getDao().getAmount();
    }

    public String getTitle() {
        return getDao().getDescription().getDefaultTranslation().getTitle();
    }

    public String getDescription() {
        return getDao().getDescription().getDefaultTranslation().getText();
    }

    DaoBatch getDao() {
        return dao;
    }
}
