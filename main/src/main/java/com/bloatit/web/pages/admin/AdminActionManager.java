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

        UPDATE_DEVELOPMENT_STATE(tr("Re calculate if this feature should passe into development.")), //
        COMPUTE_SELECTED_OFFER(tr("Re calculate the selected offer")), //
        SET_VALIDATION_DATE(tr("Update the validation Date")), //
        SET_FEATURE_IN_DEVELOPMENT(tr("Change the feature state")), //

        VALIDATE_BATCH(tr("Validate milestone if possible")), //
        FORCE_VALIDATE_BATCH(tr("Validate milestone --force !")), //
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

    public EnumSet<Action> featureActions() {
        return EnumSet.range(Action.UPDATE_DEVELOPMENT_STATE, Action.SET_FEATURE_IN_DEVELOPMENT);
    }

    public EnumSet<Action> milestoneActions() {
        return EnumSet.range(Action.VALIDATE_BATCH, Action.FORCE_VALIDATE_BATCH);
    }

}
