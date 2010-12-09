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

import test.Context;
import test.HtmlElement;
import test.Request;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlTitle;
import test.pages.master.Page;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.utils.BloatitLoaders;
import com.bloatit.web.utils.Message.Level;
import com.bloatit.web.utils.RequestParam;
import com.bloatit.web.utils.RequestParam.Role;

public class DemandPage extends Page {

    @RequestParam(name = "id", loader = BloatitLoaders.DemandLoader.class, level = Level.ERROR)
    protected Demand demand;

    @RequestParam(role = Role.PRETTY, defaultValue = "Title")
    protected String title;

    private final Request request;

    public DemandPage(final Request request) {
        super();
        this.request = request;
        this.request.setValues(this);
        addNotifications(request.getMessages());

        if (request.getMessages().hasMessage(Level.ERROR)) {
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
        return "demand.css";
    }

    public Demand getDemand() {
        return demand;
    }

    protected void generateContent() {
        Locale defaultLocale = Context.getSession().getLanguage().getLocale();
        Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);

        add(new HtmlTitle(translatedDescription.getTitle(), "pageTitle"));
        add(new DemandHeadComponent(request, demand));
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
            left.add(new DemandTabPane(request, demand));
            // Comments
            left.add(new DemandCommentListComponent(request, demand));
        }
        return left;

    }

    private HtmlElement generateBodyRight() {
        final HtmlBlock right = new HtmlBlock("rightColumn");
        {
            HtmlBlock rightBlock = new HtmlBlock("right_block");
            {
                rightBlock.add(new DemandSummaryComponent(request, demand));
            }
            right.add(rightBlock);
        }
        return right;
    }

}
