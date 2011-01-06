package com.bloatit.framework.right;

import java.util.EnumSet;

public class KudosableRight extends RightManager {

    public static class Kudos extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return authentifiedCanWrite(role, action);
        }
    }
}
