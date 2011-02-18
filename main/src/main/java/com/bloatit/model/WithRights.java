package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoGroupRight.UserGroupRight;
import com.bloatit.model.right.RightManager.OwningState;

public interface WithRights {

    public abstract OwningState getOwningState();

    public abstract EnumSet<UserGroupRight> getGroupRights();

    // make sure the user is not NOBODY.
    public abstract com.bloatit.data.DaoMember.Role getRole();

}