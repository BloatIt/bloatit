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
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Locale;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.renderer.HtmlRawTextRenderer;
import com.bloatit.model.Project;
import com.bloatit.model.Translation;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ProjectPageUrl;

@ParamContainer("project")
public final class ProjectPage extends MasterPage {

    public static final String PROJECT_FIELD_NAME = "id";

    @ParamConstraint(optionalErrorMsg=@tr("The id of the project is incorrect or missing"))
    @RequestParam(name = PROJECT_FIELD_NAME, level = Level.ERROR)
    private final Project project;

    private final ProjectPageUrl url;

    public ProjectPage(final ProjectPageUrl url) {
        super(url);
        this.url = url;
        this.project = url.getProject();
    }

    @Override
    protected void doCreate() throws RedirectException {
        session.notifyList(url.getMessages());
        if (url.getMessages().hasMessage(Level.ERROR)) {
            throw new PageNotFoundException();
        }

        project.authenticate(session.getAuthToken());

        try {

            HtmlDiv box = new HtmlDiv("padding_box");

            HtmlTitle projectName;
            projectName = new HtmlTitle(project.getName(), 1);
            box.add(projectName);

            box.add(new HtmlImage(new FileResourceUrl(project.getImage()), "float_right"));

            final Locale defaultLocale = Context.getLocalizator().getLocale();
            final Translation translatedDescription = project.getDescription().getTranslationOrDefault(defaultLocale);

            final HtmlParagraph shortDescription = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getTitle()));
            final HtmlParagraph description = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getText()));

            box.add(shortDescription);
            box.add(description);


            add(box);
        } catch (final UnauthorizedOperationException e) {
            add(new HtmlParagraph(tr("For obscure reasons, you are not allowed to see the details of this project.")));
        }
    }

    @Override
    protected String getPageTitle() {
        if (project != null) {
            try {
                return tr("Project - ") + project.getName();
            } catch (final UnauthorizedOperationException e) {
                return tr("Project - Windows 8");
            }
        }
        return tr("Member - No member");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
