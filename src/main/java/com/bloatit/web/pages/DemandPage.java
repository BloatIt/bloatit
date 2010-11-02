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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Transaction;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlString;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.PageNotFoundException;

public class DemandPage extends Page {

    private final Demand demand;

    public DemandPage(Session session, Map<String, String> parameters) {
        super(session, parameters);

        
        if (parameters.containsKey("id")) {
            Integer id = null;
            try {
                id = new Integer(parameters.get("id"));
                this.demand = DemandManager.GetDemandById(id);
            } catch (final NumberFormatException e) {
                throw new PageNotFoundException("Demand id not found " + id, null);
            }
        } else {
            demand = null;
        }

    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        if (demand == null) {
            throw new PageNotFoundException("Demand shouldn't be null", null);
        }
        this.demand = demand;
    }

    public DemandPage(Session session, Demand demand) {
        this(session, new HashMap<String, String>(), demand);
    }

    @Override
    protected HtmlComponent generateContent() {

        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translation = demand.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlContainer page = new HtmlContainer();
        
        final HtmlBlock left = new HtmlBlock("leftColumn");
        final HtmlBlock right = new HtmlBlock("rightColumn");
        page.add(new HtmlTitle(translation.getTitle(), "pageTitle"));
        page.add(left);
        page.add(right);
        
        // block avec la progression
        final HtmlBlock progress = new HtmlBlock("progress");
        right.add(progress);
        
        // get the contribution
        progress.add(new HtmlText(demand.getContribution().toPlainString()));
        
        
        
        // description
        
        // com
        
        // droite process
        
        
        

        // TODO CORRECT ME
        // HtmlTitle demandTitle = new HtmlTitle(HtmlString.Translate(session,
        // this.demand.getTitle()), "demand_title");
        // demandBlock.add(demandTitle);

        return page;

    }

    @Override
    public String getCode() {
        if (this.demand != null) {
            return new HtmlString(session).add("demand/id-" + this.demand.getId() + "/title-").secure(demand.getTitle()).toString();
        } else {
            return "demand"; // TODO Faire un système pour afficher une page
                             // d'erreur
        }
    }

    @Override
    public String getTitle() {
        return "Demand ...";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
