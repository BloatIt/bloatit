package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup.Displayable;

public class AdminActionManager {

    public enum Action implements Displayable {
        DELETE(tr("delete")), //
        RESTORE(tr("restore"));

        private final String displayName;

        Action(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }
    }

    public EnumSet<Action> userContentActions() {
        return EnumSet.range(Action.DELETE, Action.RESTORE);
    }

}
