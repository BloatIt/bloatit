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
import com.bloatit.model.Translatable;
import com.bloatit.model.Translation;
import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.web.server.Language;
import java.util.ArrayList;

public class DemandManager {

    private final static Member yoann;
    private final static ArrayList<Demand> demands;

    static {
        yoann = new Member(1, "Yoann", "yplenet@gmail.com", "yoann plénet", 150);

        demands = new ArrayList<Demand>(){
            {
                add(new Demand(1, 
                        new Translatable(new Translation("Title", new Language(), yoann, 0)),
                        new Translatable(new Translation("Description", new Language(), yoann, 0)),
                        new Translatable(new Translation("Specification", new Language(), yoann, 0)),
                        new ArrayList<Translatable>(),
                        1, yoann));
                add(new Demand(2,
                        new Translatable(new Translation("Demande 2", new Language("fr"), yoann, 0)),
                        new Translatable(new Translation("Description", new Language("fr"), yoann, 0)),
                        new Translatable(new Translation("Specification", new Language(), yoann, 0)),
                        new ArrayList<Translatable>(),
                        1, yoann));
               add(new Demand(3,
                        new Translatable(new Translation("German 3", new Language("de"), yoann, 0)),
                        new Translatable(new Translation("German demand", new Language("de"), yoann, 0)),
                        new Translatable(new Translation("This is a very nice german demand", new Language("de"), yoann, 0)),
                        new ArrayList<Translatable>(),
                        1, yoann));
                add(new Demand(4,
                        new Translatable(new Translation("Title4", new Language(), yoann, 0)),
                        new Translatable(new Translation("Description", new Language(), yoann, 0)),
                        new Translatable(new Translation("Specification", new Language(), yoann, 0)),
                        new ArrayList<Translatable>(),
                        1, yoann));
            }
        };
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
