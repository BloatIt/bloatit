/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.pages.demand;

import static com.bloatit.web.server.Context.tr;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.demand.Demand;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.DemandPageUrl;

@ParamContainer("demand")
public final class DemandPage extends Page {

    public static final String IDEA_FIELD_NAME = "id";

    @RequestParam(name = IDEA_FIELD_NAME, level = Level.ERROR)
    private final Demand demand;

    @RequestParam(role = Role.PRETTY, defaultValue = "Title", generatedFrom = "demand")
    private final String title;

    private DemandTabPane demandTabPane;
    private final DemandPageUrl url;

    public DemandPage(final DemandPageUrl url) {
        super(url);
        this.url = url;
        demand = url.getDemand();
        title = url.getTitle();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getTitle() {
        if (demand != null) {
            try {
                return demand.getTitle();
            } catch (final UnauthorizedOperationException e) {
                // Return the default one.
            }
        }
        return tr("Idea not found !");
    }

    @Override
    protected String getCustomCss() {
        return "demand.css";
    }

    public Demand getDemand() {
        return demand;
    }

    @Override
    protected void doCreate() throws RedirectException {
        addNotifications(url.getMessages());

        if (url.getMessages().hasMessage(Level.ERROR)) {
            setPageNotFound();
            return;
        }

        // The demand page is composed by 3 parts:
        // - The sumary
        // - The tab panel
        // - The comments

        add(new DemandSummaryComponent(demand));
        add(new DemandTabPane(url.getDemandTabPaneUrl(),demand));
        add(new DemandCommentListComponent(demand));



    }

}
