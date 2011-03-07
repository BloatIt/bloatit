package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Demand;
import com.bloatit.web.linkable.demands.DemandsTools;
import com.bloatit.web.linkable.projects.ProjectsTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.url.DemandPageUrl;

public class IndexDemandBlock extends HtmlDiv {


    private final HtmlTitle title;
    private final PlaceHolderElement floatRight;

    public IndexDemandBlock(Demand demand, String titleString) {
        super("index_element");

        title = new HtmlTitle(2);
        title.setCssClass("index_element_title");
        floatRight = new PlaceHolderElement();
        super.add(floatRight);
        super.add(title);

        setTitle(titleString);

        try {

            setFloatRight(ProjectsTools.getProjectLogo(demand.getProject()));

            add(new DefineParagraph(tr("Title: "), DemandsTools.getTitle(demand)).addCssClass("index_height_p"));

            add(new DefineParagraph(tr("Project: "), ProjectsTools.getProjectLink(demand.getProject())));


            add(DemandsTools.generateProgress(demand));

            add(new DemandPageUrl(demand).getHtmlLink(tr("more details...")));

            add(DemandsTools.generateDetails(demand));

        } catch (UnauthorizedOperationException e) {
        }
    }

    public void setTitle(String title) {
        this.title.addText(title);
    }

    public HtmlBranch setFloatRight(HtmlElement element) {
        floatRight.add(new HtmlDiv("float_right").add(element));
        return this;
    }
}
