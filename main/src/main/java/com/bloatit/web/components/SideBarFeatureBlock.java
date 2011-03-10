package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.projects.ProjectsTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.url.FeaturePageUrl;

public class SideBarFeatureBlock extends SideBarElementLayout {

    public SideBarFeatureBlock(Feature feature) {
        setTitle(tr("Feature abstract"));

        try {

            setFloatRight(ProjectsTools.getProjectLogo(feature.getProject()));

            add(new DefineParagraph(tr("Title: "), FeaturesTools.getTitle(feature)));

            add(new DefineParagraph(tr("Project: "), ProjectsTools.getProjectLink(feature.getProject())));
            add(new DefineParagraph(tr("Popularity: "), String.valueOf(feature.getPopularity())));

            add(new HtmlParagraph(FeaturesTools.generateProgress(feature)));

            add(new HtmlParagraph(new FeaturePageUrl(feature).getHtmlLink(tr("more details..."))));

        } catch (UnauthorizedOperationException e) {
        }
    }
}
