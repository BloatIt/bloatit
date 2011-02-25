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

/**
 * You have to implement a model manager in the model layer. The model manager
 * allows you to plug your model into the web framework.
 *
 * @author Thomas Guyard
 */
public interface AbstractModel {

    /**
     * Launch the model layer. This method will be called only once, before
     * everything else.
     */
    public abstract void initialize();

    /**
     * Shutdown the model layer. This method will be called only once, at the
     * end of the execution.
     */
    public abstract void shutdown();

    /**
     * Set the current transaction in readOnly mode. You do not have to call
     * this yourself.
     */
    public abstract void setReadOnly();

    /**
     * Open a transaction in normal (rw) mode.
     */
    public abstract void open();

    /**
     * Close the current transaction, tells to the db layer to flush everything.
     */
    public abstract void close();

    public abstract void rollback();

}
