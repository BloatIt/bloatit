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
package com.bloatit.web.pages.demand;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
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
                demand = DemandManager.getDemandById(id);
            } catch (final NumberFormatException e) {
                throw new PageNotFoundException("Demand id not found " + id, null);
            }
        } else {
            demand = null;
        }
        generateOutputParams();

    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        if (demand == null) {
            throw new PageNotFoundException("Demand shouldn't be null", null);
        }
        this.demand = demand;
        generateOutputParams();
    }

    public DemandPage(Session session, Demand demand) {
        this(session, new HashMap<String, String>(), demand);
    }

    private void generateOutputParams() {
        getParameters().add("id", new Integer(demand.getId()).toString());
        getParameters().add("title", demand.getTitle());
    }

    @Override
    public String getCode() {
        return "demand";
    }

    @Override
    public String getTitle() {
        return "Demand ...";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    public Demand getDemand() {
        return demand;
    }

    @Override
    protected HtmlComponent generateContent() {
        needCustomDesign();

        final HtmlContainer page = new HtmlContainer();
        {
            Locale defaultLocale = session.getLanguage().getLocale();
            Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);

            page.add(new HtmlTitle(translatedDescription.getTitle(), "pageTitle"));
            page.add(new DemandHeadComponent(this));
            page.add(generateBody());
        }
        return page;
    }

    private HtmlComponent generateBodyLeft() {
        final HtmlBlock left = new HtmlBlock("leftColumn");
        {
            left.add(new DemandTabPane(this));
            // Comments
            left.add(new DemandCommentListComponent(this));
        }
        return left;

    }

    private HtmlComponent generateBodyRight() {
        final HtmlBlock right = new HtmlBlock("rightColumn");
        {
            HtmlBlock rightBlock = new HtmlBlock("right_block");
            {
                rightBlock.add(new DemandSummaryComponent(this));
            }
            right.add(rightBlock);
        }
        return right;
    }

    private HtmlComponent generateBody() {
        HtmlBlock demandBody = new HtmlBlock("demand_body");
        {
            demandBody.add(generateBodyLeft());
            demandBody.add(generateBodyRight());
        }

        return demandBody;
    }
}
