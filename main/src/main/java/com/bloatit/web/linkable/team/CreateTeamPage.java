package com.bloatit.web.linkable.team;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;

/**
 * <p>
 * A page used to create a new team
 * </p>
 */
@ParamContainer("team/create")
public class CreateTeamPage extends MasterPage {
    @SuppressWarnings("unused")
    private final CreateTeamPageUrl url;

    public CreateTeamPage(final CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateMain());
        layout.addRight(new SideBarDocumentationBlock("create_team"));
        layout.addRight(new SideBarDocumentationBlock("describe_team"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    private HtmlElement generateMain() {
        final HtmlDiv box = new HtmlDiv();

        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Create a new team"), 1);
        box.add(master);

        final CreateTeamActionUrl target = new CreateTeamActionUrl();

        final FieldData nameData = target.getLoginParameter().pickFieldData();
        final FieldData contactData = target.getContactParameter().pickFieldData();
        final FieldData rightData = target.getRightParameter().pickFieldData();
        final FieldData descriptionData = target.getDescriptionParameter().pickFieldData();

        final HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), Context.tr("Name of the team: "));
        nameInput.setDefaultValue(nameData.getSuggestedValue());
        nameInput.addErrorMessages(nameData.getErrorMessages());
        nameInput.setComment(Context.tr("The public name of the team. Between 5 and 50 characters."));
        form.add(nameInput);

        final HtmlTextArea contactInput = new HtmlTextArea(contactData.getName(), Context.tr("Contact of the team: "), 10, 80);
        contactInput.setDefaultValue(contactData.getSuggestedValue());
        contactInput.addErrorMessages(contactData.getErrorMessages());
        contactInput.setComment(Context.tr("The ways to contact the team. Email, IRC channel, mailing list ... Maximum 300 characters"));
        form.add(contactInput);

        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionData.getName(), Context.tr("Description of the team"), 10, 80);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Doesn't work yet. Between 5 and 5000 characters."));
        form.add(descriptionInput);

        final HtmlDropDown rightInput = new HtmlDropDown(rightData.getName(), Context.tr("Type of the team : "));
        rightInput.setDefaultValue(rightData.getSuggestedValue());
        rightInput.addErrorMessages(rightData.getErrorMessages());
        rightInput.addDropDownElement(CreateTeamAction.PUBLIC, Context.tr("Public"));
        rightInput.addDropDownElement(CreateTeamAction.PROTECTED, Context.tr("Protected"));
        rightInput.setComment(Context.tr("Public teams can be joined by anybody without an invitation."));
        form.add(rightInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));

        return box;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Create a new team");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return CreateTeamPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = TeamsPage.generateBreadcrumb();

        breadcrumb.pushLink(new CreateTeamPageUrl().getHtmlLink(tr("Create a team")));

        return breadcrumb;
    }
}
