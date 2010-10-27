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
package com.bloatit.web.pages;

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.model.Demand;
import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlString;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.HashMap;
import java.util.Map;

public class DemandPage extends Page {

    private final Demand demand;

    public DemandPage(Session session, Map<String, String> parameters) {
        this(session, parameters, null);
    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        Demand d = null;

        if (demand == null) {
            if (parameters.containsKey("id")) {
                Integer id = null;
                try {
                    id = new Integer(parameters.get("id"));
                } catch (NumberFormatException e) {

                }
                if (id != null) {
                    try {
                        d = DemandManager.GetDemandById(id);
                    } catch (ElementNotFoundException ex) {}
                }
            }
        } else {
            d = demand;
        }
        this.demand = d;
    }

    public DemandPage(Session session, Demand demand) {
        this(session, new HashMap<String, String>(), demand);
    }

    @Override
    protected HtmlComponent generateContent() {
        if (this.demand == null) {
            return this.generateEmptyBody();
        } else {
            return this.generateNotEmptyBody();
        }
    }

    private HtmlComponent generateEmptyBody() {
        return new HtmlText("Error : Specified demand Id incorrect");
    }

    private HtmlComponent generateNotEmptyBody() {

        HtmlBlock demandBlock = new HtmlBlock("demand");

        HtmlTitle demandTitle = new HtmlTitle(HtmlString.Translate(session, this.demand.getTitle()), "demand_title");

        demandBlock.add(demandTitle);

        return demandBlock;

    }

    @Override
    public String getCode() {
        if (this.demand != null) {
            return new HtmlString(session).add("demand/id-" + this.demand.getId() + "/title-").secure(demand.getTitle()).toString();
        } else {
            return "demand"; // TODO Faire un système pour afficher une page d'erreur
        }
    }

    public String getTitle() {
        return "Demand ...";
    }
}
