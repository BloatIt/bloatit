package com.bloatit.framework.right;

import java.util.EnumSet;

public class DemandRight extends RightManager {

    public static class Offer extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

	public static class Contribute extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class DemandContent extends PublicModerable {
    }

    public static class Specification extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    public static class Comment extends Accessor {
        @Override
        protected final boolean can(final EnumSet<Role> role, final Action action) {
            return canRead(action) || authentifiedCanWrite(role, action);
        }
    }

    // public static class Translation extends Public {}

}
