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

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;

@ParamContainer("idea/docreate")
public class CreateIdeaAction extends Action {
    public final static String DESCRIPTION_CODE = "bloatit_idea_description";
    public final static String SPECIFICATION_CODE = "bloatit_idea_specification";
    public final static String PROJECT_CODE = "bloatit_idea_project";
    public final static String CATEGORY_CODE = "bloatit_idea_category";


    private CreateIdeaActionUrl url;

    public CreateIdeaAction(final CreateIdeaActionUrl url) throws RedirectException {
        super(url);
        this.url = url;

        session.notifyList(url.getMessages());
    }

    @Override
    protected String doProcess() throws RedirectException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
