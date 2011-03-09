package com.bloatit.web.linkable.projects;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.HtmlSpan;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Project;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.ProjectPageUrl;

public class ProjectsTools {
    
    public static HtmlElement getProjectLogo(Project project) throws UnauthorizedOperationException {
        HtmlDiv logoDiv = new HtmlDiv("project_logo_block");
        if (project.getImage() == null) {
            logoDiv.add(new HtmlImage(new Image("idea.png", Image.ImageType.LOCAL), tr("Project logo"), "project_logo"));
        } else {
            final FileResourceUrl imageUrl = new FileResourceUrl(project.getImage());
            logoDiv.add(new HtmlImage(imageUrl, tr("Project logo"), "project_logo"));
        }

        return logoDiv;
    }

    public static HtmlElement getProjectLink(Project project) throws UnauthorizedOperationException {
        final HtmlSpan projectSpan = new HtmlSpan("project_link");
        projectSpan.add(new ProjectPageUrl(project).getHtmlLink(project.getName()));

        return projectSpan;
    }
}
