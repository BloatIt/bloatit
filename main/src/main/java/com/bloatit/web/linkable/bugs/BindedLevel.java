package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webprocessor.Context.tr;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.webprocessor.components.form.Displayable;

public enum BindedLevel implements Displayable {
    FATAL(Level.FATAL, tr("FATAL")), MAJOR(Level.MAJOR, tr("MAJOR")), MINOR(Level.MINOR, tr("MINOR"));

    private final String label;
    private final Level level;

    private BindedLevel(Level level, String label) {
        this.level = level;
        this.label = label;
    }

    public static BindedLevel getBindedLevel(Level level) {
        return Enum.valueOf(BindedLevel.class, level.name());
    }

    @Override
    public String getDisplayName() {
        return label;
    }

    public Level getLevel() {
        return level;
    }

}
