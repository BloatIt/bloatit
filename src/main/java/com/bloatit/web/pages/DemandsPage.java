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
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DemandsPage extends Page {

    public DemandsPage(){
    }

    public DemandsPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public DemandsPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected void generateContent() {
        ArrayList<Demand> demands = DemandManager.GetAllDemands();
        this.htmlResult.write("<div class='demand_table'>");
        this.htmlResult.indent();
        for (Demand demand : demands){
            DemandPage view = new DemandPage(session, demand);
            this.htmlResult.write("<div class=\"demand_entry\">");
            this.htmlResult.indent();
            this.htmlResult.write("<p class=\"demand_title\">"+ HtmlTools.generateLink(this.session, demand.getTitle() , view) +"</p>");
            this.htmlResult.write("<p class=\"demand_description\">"+ demand.getDescription()+"</p>");
            this.htmlResult.unindent();
            this.htmlResult.write("</div>");
        }
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    public String getTitle(){
        return "View all demands - search demands";
    }

    public String getCode(){
        return "demands";
    }
}
