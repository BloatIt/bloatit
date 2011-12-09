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

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bloatit.common.Log;
import com.bloatit.data.DataManager;
import com.bloatit.data.exceptions.ElementNotFoundException;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.Hash;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.xcgiserver.RequestKey;
import com.bloatit.model.feature.FeatureList;
import com.bloatit.model.feature.TaskUpdateDevelopingState;
import com.bloatit.model.right.AuthToken;

public class Model implements com.bloatit.framework.model.Model {
    AtomicBoolean isClosed = new AtomicBoolean(true);

    public Model() {
        // do nothing
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#launch()
     */
    @Override
    public void initialize() {
        DataManager.initialize();
        Log.model().trace("Launching the Model.");
        ModelConfiguration.loadConfiguration();

        open();
        // Find the feature with selected offer that should pass into validated.
        final PageIterable<Feature> featuresToValidate = new FeatureList(DBRequests.featuresThatShouldBeValidated());
        for (final Feature feature : featuresToValidate) {
            feature.updateDevelopmentState();
        }

        // Find the feature with selected offer that should pass into validated.
        final PageIterable<Feature> featuresToValidateInTheFuture = new FeatureList(DBRequests.featuresThatShouldBeValidatedInTheFuture());
        for (final Feature feature : featuresToValidateInTheFuture) {
            new TaskUpdateDevelopingState(feature.getId(), feature.getValidationDate());
        }
        close();
        
        EventMailer.start();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#shutdown()
     */
    @Override
    public void shutdown() {
        Log.model().trace("Shutdowning the Model.");
        DataManager.shutdown();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#openReadOnly()
     */
    @Override
    public void setReadOnly() {
        Log.model().trace("This transaction is Read Only.");
        DataManager.setReadOnly();
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#open()
     */
    @Override
    public void open() {
        Log.model().trace("Open a new transaction.");
        AuthToken.unAuthenticate();
        DataManager.open();
        isClosed.set(false);
    }

    /*
     * (non-Javadoc)
     * @see com.bloatit.model.AbstractModelManager#close()
     */
    @Override
    public void close() {
        // Check temporary authenticatoin integrity
        if(AuthToken.isTemporaryAuthenticated()) {
            Log.model().fatal("The session is temporary autenticated !");
            rollback();
            return;
        }
        
        if (!isClosed.get()) {
            Log.model().trace("Close the current transaction.");
            CacheManager.clear();
            DataManager.close();
            isClosed.set(true);
        } else {
            Log.model().trace("Transaction already closed.");
        }
    }

    @Override
    public void rollback() {
        if (!isClosed.get()) {
            CacheManager.clear();
            DataManager.rollback();
            isClosed.set(true);
        } else {
            Log.model().trace("Transaction already closed.");
        }
    }

    @Override
    public void flush() {
        Log.model().trace("Flush the current transaction.");
        CacheManager.clear();
        DataManager.close();
        DataManager.open();
    }

    @Override
    public void authenticate(final RequestKey key) {
        try {
            AuthToken.authenticate(key);
        } catch (final ElementNotFoundException e) {
            Log.model().trace("authentication error.", e);
        }
        accessLog(key);
    }

    private void accessLog(final RequestKey key) {
        final String memberId = AuthToken.getMember() != null ? AuthToken.getMember().getId().toString() : "-1";
        final Session session = Context.getSession();
        String sessionKey;
        if (session != null) {
            sessionKey = Hash.shortHash(session.getShortKey());
        } else {
            sessionKey = Hash.shortHash(key.getId());
        }
        Localizator localizator = Context.getLocalizator();
        Locale locale = null;
        if (localizator != null) {
            locale = localizator.getLocale();
        }
        Log.framework().info("Access:Context: " + //
                "USER_ID='" + memberId + //
                "'; KEY='" + sessionKey + //
                "'; LANG='" + locale + "'");
    }
}
