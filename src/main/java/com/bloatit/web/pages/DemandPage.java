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

import com.bloatit.framework.DemandManager;
import com.bloatit.model.Demand;
import com.bloatit.model.exceptions.ElementNotFoundException;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.HashMap;
import java.util.Map;

public class DemandPage extends Page {

    private final Demand demand;

    public DemandPage(){
        demand=null;
    }

    public DemandPage(Session session, Map<String, String> parameters) {
        this(session, parameters, null);
    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        Demand d = null;

        if (demand == null) {
            if (parameters.containsKey("id")) {
                Integer id = new Integer(parameters.get("id"));
                if (id != null) {
                    try {
                        d = DemandManager.GetDemandById(id);
                    } catch (ElementNotFoundException ex) {
                    }
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
    protected void generateContent() {
        if (this.demand == null) {
            this.generateEmptyBody();
        } else {
            this.generateNotEmptyBody();
        }
    }

    private void generateEmptyBody() {
        this.htmlResult.write("Error : Specified demand Id incorrect");
    }

    private void generateNotEmptyBody() {
        this.htmlResult.write("<div class=\"demand\">");
        this.htmlResult.indent();
        this.htmlResult.write("<p>Demand id : "+ this.demand.getId() + "</p>" );
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    @Override
    public String getCode() {
        if (this.demand != null) {
            return "demand/id-" + this.demand.getId() + "/title-" + HtmlTools.escapeUrlString(this.demand.getTitle());
        } else {
            return "demand"; // TODO Faire un système pour afficher une page d'erreur
        }
    }

    public String getTitle(){
        return "Demand ...";
    }
}