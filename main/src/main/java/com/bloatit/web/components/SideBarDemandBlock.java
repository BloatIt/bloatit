package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.model.Demand;
import com.bloatit.web.linkable.demands.DemandsTools;
import com.bloatit.web.linkable.projects.ProjectsTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.SideBarElementLayout;
import com.bloatit.web.url.DemandPageUrl;

public class SideBarDemandBlock extends SideBarElementLayout {

    public SideBarDemandBlock(Demand demand) {
        setTitle(tr("Demand abstract"));

        try {

            setFloatRight(ProjectsTools.getProjectLogo(demand.getProject()));

            add(new DefineParagraph(tr("Title: "), DemandsTools.getTitle(demand)));

            add(new DefineParagraph(tr("Project: "), ProjectsTools.getProjectLink(demand.getProject())));
            add(new DefineParagraph(tr("Popularity: "), String.valueOf(demand.getPopularity())));

            add(new HtmlParagraph(DemandsTools.generateProgress(demand)));

            // add(new HtmlParagraph(DemandsTools.generateDetails(demand)));

            add(new HtmlParagraph(new DemandPageUrl(demand).getHtmlLink(tr("more details..."))));

        } catch (UnauthorizedOperationException e) {
        }
    }
}
