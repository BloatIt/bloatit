package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoMember.Role;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.model.Demand;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;
import com.bloatit.web.url.DemandPageUrl;

@ParamContainer("administration")
public class AdministrationPage extends LoggedPage {

    @RequestParam(level = Level.ERROR)
    @ParamConstraint(optional = true)
    private final String filter;

    @RequestParam(level = Level.ERROR)
    @ParamConstraint(optional = true)
    private final Boolean hasSelectedOffer;

    public AdministrationPage(AdministrationPageUrl url) {
        super(url);
        this.filter = url.getFilter();
        this.hasSelectedOffer = url.getHasSelectedOffer();
    }

    private void addCell(HtmlBranch node, Object obj) {
        if (obj != null) {
            node.add(new HtmlGenericElement("td").addText(obj.toString()));
        } else {
            node.add(new HtmlGenericElement("td").addText("null"));
        }
    }

    @Override
    protected String getPageTitle() {
        return "test";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {

        PlaceHolderElement everything = new PlaceHolderElement();
        try {
            if (session.getAuthToken().getMember().getRole() != Role.ADMIN) {
                session.notifyError(tr("You have to be the administrator to access this page."));
                throw new RedirectException(new PageNotFoundUrl());
            }

            PageIterable<Demand> demands = null;

            // Print the list of actions.
            if (demands == null) {
                return everything;
            }

            HtmlForm form = new HtmlForm(new AdministrationActionUrl().urlString());
            everything.add(form);
            HtmlGenericElement table = new HtmlGenericElement("table");
            table.addAttribute("border", "1");

            HtmlGenericElement header = new HtmlGenericElement("tr");
            table.add(header);
            addCell(header, tr("*"));
            addCell(header, tr("Id"));
            addCell(header, tr("Title"));
            addCell(header, tr("Contribution"));
            addCell(header, tr("Contributions.size"));
            addCell(header, tr("Offers"));
            addCell(header, tr("SelectedOffer"));
            addCell(header, tr("ValidatedOffer"));
            addCell(header, tr("CreationDate"));
            addCell(header, tr("Author"));
            addCell(header, tr("AsGroup"));
            addCell(header, tr("Popularity"));
            addCell(header, tr("DemandState"));

            // TODO Sort.
            // TODO Filters
            // TODO action

            for (Demand demand : demands) {
                // demand.cancelDevelopment();
                // demand.validateCurrentBatch(true);
                // demand.voteDown();
                // demand.voteUp();

                HtmlGenericElement line = new HtmlGenericElement("tr");
                table.add(line);
                line.add(new HtmlGenericElement("td").add(new HtmlCheckbox("ids", LabelPosition.AFTER).addAttribute("value", demand.getId()
                        .toString())));

                line.add(new HtmlGenericElement("td").add(new HtmlLink(new DemandPageUrl(demand).urlString(), demand.getId().toString())));
                addCell(line, demand.getTitle());
                addCell(line, demand.getContribution());
                addCell(line, demand.getContributions().size());
                addCell(line, demand.getSelectedOffer());
                addCell(line, demand.getValidatedOffer());
                addCell(line, demand.getOffers().size());
                addCell(line, demand.getCreationDate());
                addCell(line, demand.getAuthor());
                addCell(line, demand.getAsGroup());
                addCell(line, demand.getPopularity());
                addCell(line, demand.getDemandState());
            }

            form.add(table);
            form.add(new HtmlSubmit("Ok !"));

        } catch (UnauthorizedOperationException e) {
            session.notifyError(tr("You have to be the administrator to access this page."));
            throw new RedirectException(new PageNotFoundUrl());
        }
        return everything;
    }

    @Override
    public String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }
}
