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
package com.bloatit.framework.model;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.xcgiserver.RequestKey;

/**
 * Thread safe class. It calls to the model initialization and open close
 * methods.
 */
public class ModelAccessor {
    /**
     * This static variable is protected by the {@link #mutex}.
     */
    private static Model model = null;

    private static void setModelManager(final Model manager) {
        if (model == null) {
            model = manager;
        } else {
            throw new BadProgrammerException("You can set the modelManager only once !");
        }
    }

    /**
     * This method is the init method. Call it only once, in a non MultiThreaded
     * environment.
     * 
     * @see com.bloatit.framework.model.Model#initialize()
     */
    public static void initialize(final Model manager) {
        setModelManager(manager);
        model.initialize();
    }

    /**
     * @see com.bloatit.framework.model.Model#shutdown()
     */
    public static void shutdown() {
        if (model != null) {
            model.shutdown();
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#setReadOnly()
     */
    public static void setReadOnly() {
        model.setReadOnly();
    }

    /**
     * @see com.bloatit.framework.model.Model#open()
     */
    public static void open() {
        model.open();
    }

    public static void authenticate(final RequestKey key) {
        model.authenticate(key);
    }

    /**
     * @see com.bloatit.framework.model.Model#close()
     */
    public static void close() {
        model.close();
    }

    /**
     * @see com.bloatit.framework.model.Model#flush()
     */
    public static void flush() {
        model.flush();
    }

    /**
     * @see com.bloatit.framework.model.Model#close()
     */
    public static void rollback() {
        model.rollback();
    }
}
