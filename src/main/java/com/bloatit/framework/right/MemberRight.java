package com.bloatit.framework.right;

import java.util.EnumSet;

public class MemberRight extends RightManager {

    public static class GroupList extends Accessor {
        @Override
        protected boolean can(EnumSet<Role> role, Action action) {
            return canRead(action) || ownerCanWrite(role, action) || ownerCanDelete(role, action);
        }
    }

    public static class Karma extends Public {}

    public static class Password extends Private {}

    public static class Name extends Public {}

}
