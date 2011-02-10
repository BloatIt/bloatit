package com.bloatit.model;

public interface UnlockableInterface {

    /**
     * You have to authenticate a Unlockable before using a method which look for an
     * authenticated user.
     *
     * @param authToken it represent the user try to access this Unlockable. It can be
     *        null for a non authenticated user.
     */
    void authenticate(final AuthToken authToken);

    // TODO getRole.

}