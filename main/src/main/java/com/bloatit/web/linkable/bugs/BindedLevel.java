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
package com.bloatit.web.linkable.bugs;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum BindedLevel implements Displayable {
    FATAL(Level.FATAL, tr("FATAL")), MAJOR(Level.MAJOR, tr("MAJOR")), MINOR(Level.MINOR, tr("MINOR"));

    private final String label;
    private final Level level;

    private BindedLevel(final Level level, final String label) {
        this.level = level;
        this.label = label;
    }

    protected static BindedLevel getBindedLevel(final Level level) {
        return Enum.valueOf(BindedLevel.class, level.name());
    }

    @Override
    public String getDisplayName() {
        return Context.tr(label);
    }

    public Level getLevel() {
        return level;
    }

    //Fake tr
    private static String tr(String fake) {
        return fake;
    }

}
