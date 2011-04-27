/*
 * 
 */
package com.bloatit.model;

import com.bloatit.data.DaoTeam;

public class DaoGetter {
    public static DaoTeam getTeam(final Team team) {
        return team != null ? team.getDao() : null;
    }
}
