package com.bloatit.framework.right;

public class AccountRight extends RightManager {

    public static class Transaction extends Private {
        // nothing this is just a rename.
    }

    public static class Amount extends PublicReadOnly {
        // nothing this is just a rename.
    }

    public static class Comment extends PublicReadOnly {
        // nothing this is just a rename.
    }

    public static class LastModificationDate extends PrivateReadOnly {
        // nothing this is just a rename.
    }

    public static class CreationDate extends PublicReadOnly {
        // nothing this is just a rename.
    }

    public static class Actor extends PublicReadOnly {
        // nothing this is just a rename.
    }
}
