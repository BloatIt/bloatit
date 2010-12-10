package com.bloatit.web.utils;

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;

public class BloatitLoaders {

    static public class DemandLoader extends Loader<Demand> {
        @Override
        public Demand fromString(final String data) {
            return DemandManager.getDemandById(Integer.valueOf(data));
        }
    }

}
