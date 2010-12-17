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
package com.bloatit.web.html.pages.idea;

import java.util.Locale;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.IdeaPageUrl;

@ParamContainer("idea")
public class IdeaPage extends Page {

    public static final String IDEA_FIELD_NAME = "id";

    @RequestParam(name = IDEA_FIELD_NAME, level = Level.ERROR)
    protected Demand idea;

    @RequestParam(role = Role.PRETTY, defaultValue = "Title", generatedFrom = "idea")
    protected String title;

    private IdeaTabPane demandTabPane;
    private final IdeaPageUrl url;

    public IdeaPage(final IdeaPageUrl url) {
        super();
        this.url = url;
        idea = url.getIdea();
        title = url.getTitle();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getTitle() {
        if (idea != null) {
            return idea.getTitle();
        }
        return "Idea not found !";
    }

    @Override
    protected String getCustomCss() {
        return "idea.css";
    }

    public Demand getDemand() {
        return idea;
    }

    @Override
    public void create() throws RedirectException {
        super.create();
        addNotifications(url.getMessages());

        if (url.getMessages().hasMessage(Level.ERROR)) {
            setPageNotFound();
            return;
        }
        
        final Locale defaultLocale = Context.getSession().getLanguage().getLocale();
        final Translation translatedDescription = idea.getDescription().getTranslationOrDefault(defaultLocale);

        add(new HtmlTitleBlock(translatedDescription.getTitle(), 1).setCssClass("pageTitle"));
        add(new IdeaHeadComponent(idea));
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
            demandTabPane = new IdeaTabPane(url.getDemandTabPaneUrl(), idea);
            left.add(demandTabPane);
            // Comments
            left.add(new IdeaCommentListComponent(idea));
        }
        return left;

    }

    private HtmlElement generateBodyRight() {
        final HtmlDiv right = new HtmlDiv("rightColumn");
        {
            final HtmlDiv rightBlock = new HtmlDiv("right_block");
            {
                rightBlock.add(new IdeaSummaryComponent(idea));
            }
            right.add(rightBlock);
        }
        return right;
    }

}
