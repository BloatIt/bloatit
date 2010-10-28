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

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
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
    protected HtmlComponent generateContent() {
        ArrayList<Demand> demands = DemandManager.GetAllDemands();
        TranslationManipulator tm = new TranslationManipulator(session.getPreferredLangs());
        
        HtmlBlock demandTableAll = new HtmlBlock("demand_table");

        demandTableAll.add(new HtmlText("<a href=\"demands/show-all\">Show me demands not in my language too (Doesn't work yet)</a>"));
        boolean showAll = false;
        if(this.parameters.containsKey("show") && this.parameters.get("show").equals("all")){
            showAll = true;
        }
        
        HtmlBlock demandTable = new HtmlBlock("demand_table");
        
//        TODO correct me
//        for (Demand demand : demands){
//            Translation title = tm.tr(demand.getTitle());
//            Translation decription = tm.tr(demand.getDescription());
//
//            if(showAll || title != null){
//                DemandPage view = new DemandPage(session, demand);
//                HtmlBlock demandEntry = new HtmlBlock("demand_entry");
//
//                demandEntry.add(new HtmlText("demand_title",  HtmlTools.generateLink(this.session, title.getTitle() ,view)));
//                demandEntry.add(new HtmlText("demand_description",  decription.getText()));
//                demandEntry.add(new HtmlText("demand_author",  demand.getAuthor().getLogin()));
//                demandEntry.add(new HtmlText("demand_list_langs",  "Langs :"));
//                
//                for(Language l : demand.getDescription().getAvailableLangs() ){
//                    demandEntry.add(new HtmlText("demand_lang",  l.getCode()));
//                }
//                demandTable.add(demandEntry);
//            }
//        }
        
        HtmlContainer page = new HtmlContainer();
        page.add(demandTableAll);
        page.add(demandTable);
        
        return page;

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
