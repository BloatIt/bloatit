package com.bloatit.model;

import com.bloatit.data.DaoConfiguration;

public class Configuration {
    DaoConfiguration dao;

    private Configuration() {
        dao = DaoConfiguration.getInstance();
    }

    public static Configuration getInstance() {
        return new Configuration();
    }

    public synchronized int getNextMercanetTransactionId() {
        final int id = dao.getMercanetTransactionId();
        if (id == 999999) {
            dao.setMercanetTransactionId(0);
        } else {
            dao.setMercanetTransactionId(id + 1);
        }
        return id;
    }
}
