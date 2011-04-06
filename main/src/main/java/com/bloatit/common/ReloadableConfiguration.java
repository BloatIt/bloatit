/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.common;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Mother class for all configurations
 */
public abstract class ReloadableConfiguration {
    private static Set<ReloadableConfiguration> configurations = new HashSet<ReloadableConfiguration>();
    private Date lastReloadDate;

    /**
     * Creates a new configuration
     */
    public ReloadableConfiguration() {
        configurations.add(this);
        this.lastReloadDate = new Date();
    }

    /**
     * @return the last of date reload for this configuration
     */
    public Date getLastReload() {
        return lastReloadDate;
    }

    /**
     * @return a list of all configurations active on the system
     */
    public static Set<ReloadableConfiguration> getConfigurations() {
        return configurations;
    }

    /**
     * Reloads the configuration
     */
    public final void reload() {
        this.lastReloadDate = new Date();
        doReload();
    }

    /**
     * Finds the name of the configuration file
     * <p>
     * NOTE: This name should be unique among all configuration files
     * </p>
     * 
     * @return The name of the configuration file
     */
    public abstract String getName();

    /**
     * Executes the reloading action
     */
    protected abstract void doReload();
}
