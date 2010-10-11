/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.framework;

import com.bloatit.model.Demand;
import com.bloatit.model.Member;
import com.bloatit.model.exceptions.ElementNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemandManager {

    private final static Member yoann;
    private final static ArrayList<Demand> demands;

    static {
        yoann = new Member(1, "Yoann", "yplenet@gmail.com", "yoann plénet", 150);

        Demand[] tab = {
            new Demand(1, "Title", "Description", "Specif", 1, yoann),
            new Demand(2, "Demand 2", "Description2 ", "Specif2", 100, yoann),
            new Demand(3, "Demand 3", "Description3", "Specif3", 3, yoann),
            new Demand(4, "Demand 4", "Description4", "Specif4", 5, yoann)
        };

        demands = (ArrayList<Demand>) Arrays.asList(tab);
    }

    /**
     * 
     * @return
     */
    public static ArrayList<Demand> GetAllDemands() {
        return demands;
    }

    /**
     * 
     * @param id
     * @return
     * @throws ElementNotFoundException
     */
    public static Demand GetDemandById(int id) throws ElementNotFoundException {
        for (Demand d : demands) {
            if (d.getId() == id) {
                return d;
            }
        }
        throw new ElementNotFoundException();
    }
}
