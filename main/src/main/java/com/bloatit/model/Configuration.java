package com.bloatit.model;

import com.bloatit.data.DaoConfiguration;

public class Configuration {
    DaoConfiguration dao;
    private static Configuration instance;

    private Configuration() {
        dao = DaoConfiguration.getInstance();
    }

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public synchronized int getNextMercanetTransactionId() {
        int id = dao.getMercanetTransactionId();
        if (id == 999999) {
            dao.setMercanetTransactionId(0);
        } else {
            dao.setMercanetTransactionId(id + 1);
        }
        return id;
    }
}
