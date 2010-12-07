/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package test.pages.demand;

import java.util.Locale;
import java.util.Map;

import test.Context;
import test.HtmlElement;
import test.Page;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlTitle;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.utils.BloatitLoaders;
import com.bloatit.web.utils.Message.Level;
import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.RequestParam.Role;

public class DemandPage extends Page {

    private final Request request;

    public static class Request extends test.Request {
        @RequestParam(name = "id", loader = BloatitLoaders.DemandLoader.class, level = Level.ERROR)
        protected Demand demand;
        
        @RequestParam(role = Role.PRETTY, defaultValue="Title")
        protected String title;
        
        @RequestParam(name = "demand_tab_key", defaultValue="description_tab")
        protected String activeTabKey;
        
        public Request() {
            super("demand");
        }

        public Request(Map<String, String> parameters) {
            super("demand", parameters);
            this.setValues();
        }
    }

    public DemandPage(Map<String, String> parameters) {
        super();
        request = new Request(parameters);
        addNotifications(request.getMessages());
        
        if(request.getMessages().hasMessage(Level.ERROR)){
            setPageNotFound();
            return;
        }
        
        generateContent();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getCustomCss() {
        return request.getPageName() + ".css";
    }

    public Demand getDemand() {
        return request.demand;
    }

    protected void generateContent() {
            Locale defaultLocale = Context.getSession().getLanguage().getLocale();
            Translation translatedDescription = request.demand.getDescription().getTranslationOrDefault(defaultLocale);

            add(new HtmlTitle(translatedDescription.getTitle(), "pageTitle"));
            add(new DemandHeadComponent(request));
            add(generateBody());
    }
    
    private HtmlElement generateBody() {
        HtmlBlock demandBody = new HtmlBlock("demand_body");
        {
            demandBody.add(generateBodyLeft());
            demandBody.add(generateBodyRight());
        }
        return demandBody;
    }

    private HtmlElement generateBodyLeft() {
        final HtmlBlock left = new HtmlBlock("leftColumn");
        {
            left.add(new DemandTabPane(request));
            // Comments
            left.add(new DemandCommentListComponent(request));
        }
        return left;

    }

    private HtmlElement generateBodyRight() {
        final HtmlBlock right = new HtmlBlock("rightColumn");
        {
            HtmlBlock rightBlock = new HtmlBlock("right_block");
            {
                rightBlock.add(new DemandSummaryComponent(request));
            }
            right.add(rightBlock);
        }
        return right;
    }


}
