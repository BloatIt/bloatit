package com.bloatit.web.linkable.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.web.pages.master.MasterPage;
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
    protected void doCreate() throws RedirectException {
        final HtmlDiv box = new HtmlDiv("padding_box");
        add(box);
        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Create a new team"), 1);
        box.add(master);

        final CreateTeamActionUrl target = new CreateTeamActionUrl();

        final FieldData nameData = target.getLoginParameter().pickFieldData();
        final FieldData contactData = target.getContactParameter().pickFieldData();
        final FieldData rightData = target.getRightParameter().pickFieldData();
        final FieldData descriptionData = target.getDescriptionParameter().pickFieldData();

        final HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        final HtmlTextField nameInput = new HtmlTextField(nameData.getName(), Context.tr("Name of the group: "));
        nameInput.setDefaultValue(nameData.getSuggestedValue());
        nameInput.addErrorMessages(nameData.getErrorMessages());
        nameInput.setComment(Context.tr("The public name of the group. Between 5 and 50 characters."));
        form.add(nameInput);

        final HtmlTextArea contactInput = new HtmlTextArea(contactData.getName(), Context.tr("Contact of the group: "), 10, 80);
        contactInput.setDefaultValue(contactData.getSuggestedValue());
        contactInput.addErrorMessages(contactData.getErrorMessages());
        contactInput.setComment(Context.tr("The ways to contact the group. Email, IRC channel, mailing list ... Maximum 300 characters"));
        form.add(contactInput);

        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionData.getName(), Context.tr("Description of the group"), 10, 80);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Doesn't work yet. Between 5 and 5000 characters."));
        form.add(descriptionInput);

        final HtmlDropDown rightInput = new HtmlDropDown(rightData.getName(), Context.tr("Type of the group : "));
        rightInput.setDefaultValue(rightData.getSuggestedValue());
        rightInput.addErrorMessages(rightData.getErrorMessages());
        rightInput.addDropDownElement(CreateTeamAction.PUBLIC, Context.tr("Public"));
        rightInput.addDropDownElement(CreateTeamAction.PROTECTED, Context.tr("Protected"));
        rightInput.setComment(Context.tr("Public groups can be joined by anybody without an invitation."));
        form.add(rightInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Create a new team");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
