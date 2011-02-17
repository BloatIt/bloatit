package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup.Displayable;

public class AdminActionManager {

    public enum Action implements Displayable {
        DELETE(tr("delete")), //
        RESTORE(tr("restore")), //
        LOCK(tr("lock votes")), //
        UNLOCK(tr("unlock votes")), //
        SETSTATE(tr("Change the state")), //
        COMPUTE_SELECTED_OFFER(tr("Re calculate the selected offer")), //
        SET_VALIDATION_DATE(tr("Update the validation Date")), //
        SET_DEMAND_STATE(tr("Change the demand state")), //
        ;

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
    public EnumSet<Action> kudosableActions() {
        return EnumSet.range(Action.LOCK, Action.SETSTATE);
    }
    public EnumSet<Action> demandActions() {
        return EnumSet.range(Action.COMPUTE_SELECTED_OFFER, Action.SET_DEMAND_STATE);
    }

}
