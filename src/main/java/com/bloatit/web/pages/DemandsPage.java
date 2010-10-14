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
import com.bloatit.model.Translation;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.server.Language;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.TranslationManipulator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DemandsPage extends Page {

    public DemandsPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public DemandsPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected void generateContent() {
        ArrayList<Demand> demands = DemandManager.GetAllDemands();
        TranslationManipulator tm = new TranslationManipulator(session.getPreferredLangs());
        this.htmlResult.write("<div class='demand_table'>");
        this.htmlResult.write("<a href=\"demands/show-all\">Show me demands not in my language too (Doesn't work yet)</a>");
        boolean showAll = false;
        if(this.parameters.containsKey("show") && this.parameters.get("show").equals("all")){
            showAll = true;
        }
        this.htmlResult.write("</div>");
        
        this.htmlResult.write("<div class='demand_table'>");
        this.htmlResult.indent();
        for (Demand demand : demands){
            Translation title = tm.tr(demand.getTitle());
            Translation decription = tm.tr(demand.getDescription());

            if(showAll || title != null){
                DemandPage view = new DemandPage(session, demand);
                this.htmlResult.write("<div class=\"demand_entry\">");
                this.htmlResult.indent();
                this.htmlResult.write("<p class=\"demand_title\">"+ HtmlTools.generateLink(this.session, title.getEntry() ,view) +"</p>");
                this.htmlResult.write("<p class=\"demand_description\">"+ decription.getEntry() +"</p>");
                this.htmlResult.write("<p class=\"demand_author\">Author : "+ demand.getAuthor().getLogin() +"</p>");
                this.htmlResult.write("<p class=\"demand_list_langs\">Langs : ");
                for(Language l : demand.getDescription().getAvailableLangs() ){
                    this.htmlResult.write(" <span class=\"demand_lang\">"+ l.getCode() +"</span>");
                }
                this.htmlResult.unindent();
                this.htmlResult.write("</div>");
            }
        }
        this.htmlResult.unindent();
        this.htmlResult.write("</div>");
    }

    @Override
    public String getTitle(){
        return "View all demands - search demands";
    }

    @Override
    public String getCode(){
        return "demands";
    }
}
