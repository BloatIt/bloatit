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
import test.Request;
import test.html.HtmlElement;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlTitleBlock;
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

    public DemandPage(final Request request) {
        super(request);
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

    private void generateContent() {
        final Locale defaultLocale = Context.getSession().getLanguage().getLocale();
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);

        add(new HtmlTitleBlock(translatedDescription.getTitle()).setCssClass("pageTitle"));
        add(new DemandHeadComponent(request, demand));
        add(generateBody());
    }

    private HtmlElement generateBody() {
        final HtmlDiv demandBody = new HtmlDiv("demand_body");
        {
            demandBody.add(generateBodyLeft());
            demandBody.add(generateBodyRight());
        }
        return demandBody;
    }

    private HtmlElement generateBodyLeft() {
        final HtmlDiv left = new HtmlDiv("leftColumn");
        {
            left.add(new DemandTabPane(request, demand));
            // Comments
            left.add(new DemandCommentListComponent(request, demand));
        }
        return left;

    }

    private HtmlElement generateBodyRight() {
        final HtmlDiv right = new HtmlDiv("rightColumn");
        {
            final HtmlDiv rightBlock = new HtmlDiv("right_block");
            {
                rightBlock.add(new DemandSummaryComponent(request, demand));
            }
            right.add(rightBlock);
        }
        return right;
    }

}
