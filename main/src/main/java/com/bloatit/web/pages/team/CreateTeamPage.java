package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
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
    private CreateTeamPageUrl url;

    public CreateTeamPage(CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv box = new HtmlDiv("padding_box");
        add(box);
        HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Create a new team"), 1);
        box.add(master);

        CreateTeamActionUrl target = new CreateTeamActionUrl();

        FormFieldData<String> name = target.getLoginParameter().formFieldData();
        FormFieldData<String> contact = target.getContactParameter().formFieldData();
        FormFieldData<String> right = target.getRightParameter().formFieldData();
        FormFieldData<String> description = target.getDescriptionParameter().formFieldData();

        HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        HtmlTextField nameInput = new HtmlTextField(name, Context.tr("Name of the group: "));
        nameInput.setComment(Context.tr("The public name of the group. Between 5 and 5000 characters."));
        form.add(nameInput);

        HtmlTextArea contactInput = new HtmlTextArea(contact, Context.tr("Contact of the group: "), 10, 80);
        contactInput.setComment(Context.tr("The ways to contact the group. Email, IRC channel, mailing list ... Maximum 300 characters"));
        form.add(contactInput);

        HtmlTextArea descriptionInput = new HtmlTextArea(description, Context.tr("Description of the group"), 10, 80);
        descriptionInput.setComment(Context.tr("Doesn't work yet. Between 5 and 5000 characters."));
        form.add(descriptionInput);

        HtmlDropDown rightInput = new HtmlDropDown(right, Context.tr("Type of the group : "));
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
