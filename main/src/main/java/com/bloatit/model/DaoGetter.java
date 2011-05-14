/*
 * 
 */
package com.bloatit.model;

import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;

public class DaoGetter {
    public static DaoTeam get(final Team team) {
        return team != null ? team.getDao() : null;
    }

    public static DaoSoftware get(final Software software) {
        return software != null ? software.getDao() : null;
    }
}
