package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.Demand;
import com.bloatit.web.linkable.demands.DemandsTools;
import com.bloatit.web.linkable.projects.ProjectsTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.pages.master.SideBarElementLayout;

public class SideBarDemandBlock extends SideBarElementLayout {

    public SideBarDemandBlock(Demand demand) {
        setTitle(tr("Demand abstract"));

        try {

            setFloatRight(ProjectsTools.getProjectLogo(demand.getProject()));

            add(new DefineParagraph(tr("Project: "), ProjectsTools.getProjectLink(demand.getProject())));

            add(new DefineParagraph(tr("Title: "), DemandsTools.getTitle(demand)));


        } catch (UnauthorizedOperationException e) {
        }
    }
}
