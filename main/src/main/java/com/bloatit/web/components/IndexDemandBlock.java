package com.bloatit.web.components;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.HighlightDemand;
import com.bloatit.web.linkable.demands.DemandsTools;
import com.bloatit.web.linkable.projects.ProjectsTools;
import com.bloatit.web.pages.master.DefineParagraph;
import com.bloatit.web.url.DemandPageUrl;

public class IndexDemandBlock extends HtmlDiv {

    private final PlaceHolderElement floatRight;

    public IndexDemandBlock(HighlightDemand highlightDemand) {
        super("index_element");

        add(new HtmlTitle(highlightDemand.getReason(), 2));
        HtmlDiv indexBodyElement = new HtmlDiv("index_body_element");
        add(indexBodyElement);
        floatRight = new PlaceHolderElement();
        indexBodyElement.add(floatRight);

        try {

            setFloatRight(ProjectsTools.getProjectLogo(highlightDemand.getDemand().getProject()));

            indexBodyElement.add(new HtmlTitle(new DemandPageUrl(highlightDemand.getDemand()).getHtmlLink(DemandsTools.getTitle(highlightDemand.getDemand())),
                                               3));

            indexBodyElement.add(new DefineParagraph(tr("Project: "), ProjectsTools.getProjectLink(highlightDemand.getDemand().getProject())));

            indexBodyElement.add(DemandsTools.generateProgress(highlightDemand.getDemand(), true));

            indexBodyElement.add(new DemandPageUrl(highlightDemand.getDemand()).getHtmlLink(tr("more details...")));

            indexBodyElement.add(DemandsTools.generateDetails(highlightDemand.getDemand()));

        } catch (UnauthorizedOperationException e) {
        }
    }

    public HtmlBranch setFloatRight(HtmlElement element) {
        floatRight.add(new HtmlDiv("float_right").add(element));
        return this;
    }
}
