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
package com.bloatit.web.actions;

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import java.util.Locale;

@ParamContainer("idea/docreate")
public class CreateIdeaAction extends Action {

    public final static String DESCRIPTION_CODE = "bloatit_idea_description";
    public final static String SPECIFICATION_CODE = "bloatit_idea_specification";
    public final static String PROJECT_CODE = "bloatit_idea_project";
    public final static String CATEGORY_CODE = "bloatit_idea_category";
    public final static String LANGUAGE_CODE = "bloatit_idea_lang";

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    private String description;

    @RequestParam(name = SPECIFICATION_CODE, role = Role.POST)
    private String specification;

    @RequestParam(name = PROJECT_CODE,defaultValue="VLC", role = Role.POST)
    private String project;

    @RequestParam(name = CATEGORY_CODE,defaultValue="Bug" , role = Role.POST)
    private String category;

    @RequestParam(name = LANGUAGE_CODE, role = Role.POST)
    private String lang;
    
    private CreateIdeaActionUrl url;

    public CreateIdeaAction(final CreateIdeaActionUrl url) throws RedirectException {
        super(url);
        this.url = url;

        this.description = url.getDescription();
        this.specification = url.getSpecification();
        this.project = url.getProject();
        this.category = url.getCategory();
        this.lang = url.getLang();

        session.notifyList(url.getMessages());
    }

    @Override
    protected String doProcess() throws RedirectException {
        if (!DemandManager.canCreate(session.getAuthToken())) {
            session.notifyError(session.tr("You must be logged in to create an idea."));
            return new LoginPageUrl().urlString();
        }
        // TODO : Authenticate for demand creation
        Locale langLocale = new Locale(lang);
        Demand d = new Demand(session.getAuthToken().getMember(), langLocale, description, specification);
       
        d.authenticate(session.getAuthToken());

        IdeaPageUrl to = new IdeaPageUrl(d);

        return to.urlString();
    }
}