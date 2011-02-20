package com.bloatit.model.right;

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.data.DaoMember.Role;

public interface RestrictedInterface {

    public abstract void authenticate(AuthToken token);

    /**
     * Checks for group privilege.
     * 
     * @param right the right
     * @return true, if successful
     */
    public abstract boolean hasGroupPrivilege(UserGroupRight right);

    /**
     * Checks for user privilege.
     * 
     * @param role the role
     * @return true, if successful
     */
    public abstract boolean hasUserPrivilege(Role role);

    /**
     * Checks if is authenticated.
     * 
     * @return true, if is authenticated
     */
    public abstract boolean isAuthenticated();

    /**
     * Checks if is owner.
     * 
     * @return true, if is owner
     */
    public abstract boolean isOwner();

    /**
     * Checks if is nobody.
     * 
     * @return true, if is nobody
     */
    public abstract boolean isNobody();

}
