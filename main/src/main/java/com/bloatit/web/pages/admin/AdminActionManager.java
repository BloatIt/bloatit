package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.framework.webserver.components.form.Displayable;

public class AdminActionManager {

    public enum Action implements Displayable {
        DELETE(tr("delete")), //
        RESTORE(tr("restore")), //

        LOCK(tr("lock votes")), //
        UNLOCK(tr("unlock votes")), //
        SETSTATE(tr("Change the state")), //

        UPDATE_DEVELOPMENT_STATE(tr("Re calculate if this demand should passe into development.")), //
        COMPUTE_SELECTED_OFFER(tr("Re calculate the selected offer")), //
        SET_VALIDATION_DATE(tr("Update the validation Date")), //
        SET_DEMAND_STATE(tr("Change the demand state")), //

        SET_BATCH_STATE(tr("Change the batch state")), //
        ;

        private final String displayName;

        Action(final String displayName) {
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
        return EnumSet.range(Action.UPDATE_DEVELOPMENT_STATE, Action.SET_DEMAND_STATE);
    }

    public EnumSet<Action> batchActions() {
        return EnumSet.of(Action.SET_BATCH_STATE);
    }

}
