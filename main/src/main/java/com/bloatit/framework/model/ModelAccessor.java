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

import java.util.concurrent.Semaphore;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

/**
 * Thread safe class. It calls to the model initialization and open close
 * methods.
 */
public class ModelAccessor {
    /**
     * Mutex that protect this class. Every public method is callable in a
     * multi-threaded environment.
     */
    private static Semaphore mutex = new Semaphore(1);

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
        try {
            mutex.acquire();
            setModelManager(manager);
            model.initialize();
        } catch (final InterruptedException e) {
            throw new BadProgrammerException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#shutdown()
     */
    public static void shutdown() {
        if (model != null) {
            try {
                mutex.acquire();
                model.shutdown();
            } catch (final InterruptedException e) {
                throw new BadProgrammerException(e);
            } finally {
                mutex.release();
            }
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#setReadOnly()
     */
    public static void setReadOnly() {
        try {
            mutex.acquire();
            model.setReadOnly();
        } catch (final InterruptedException e) {
            throw new BadProgrammerException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#open()
     */
    public static void open() {
        try {
            mutex.acquire();
            model.open();
        } catch (final InterruptedException e) {
            throw new BadProgrammerException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#close()
     */
    public static void close() {
        try {
            mutex.acquire();
            model.close();
        } catch (final InterruptedException e) {
            throw new BadProgrammerException(e);
        } finally {
            mutex.release();
        }
    }

    /**
     * @see com.bloatit.framework.model.Model#close()
     */
    public static void rollback() {
        try {
            mutex.acquire();
            model.rollback();
        } catch (final InterruptedException e) {
            throw new BadProgrammerException(e);
        } finally {
            mutex.release();
        }
    }
}
