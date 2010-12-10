package com.bloatit.framework.right;

import java.util.EnumSet;

public class DemandRight extends RightManager {

    public static class Contribute extends Accessor {
        @Override
        protected boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || canWrite(action);
        }
    }

    public static class DemandContent extends PublicModerable {
    }

    public static class Specification extends Public {
    }

    public static class Comment extends PublicModerable {
    }

    // public static class Translation extends Public {}

}
