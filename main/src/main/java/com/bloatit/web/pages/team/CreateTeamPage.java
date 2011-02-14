package com.bloatit.web.pages.team;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSimpleDropDown;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.web.actions.CreateTeamAction;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;

@ParamContainer("team/create")
public class CreateTeamPage extends MasterPage {
    private CreateTeamPageUrl url;

    public CreateTeamPage(CreateTeamPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlDiv master = new HtmlDiv("padding_box");
        add(master);

        CreateTeamActionUrl target = new CreateTeamActionUrl();

        FormFieldData<String> name = target.getLoginParameter().createFormFieldData();
        FormFieldData<String> email = target.getEmailParameter().createFormFieldData();
        FormFieldData<String> right = target.getRightParameter().createFormFieldData();

        HtmlForm form = new HtmlForm(target.urlString());
        master.add(form);

        HtmlTextField nameInput = new HtmlTextField(name, Context.tr("Name of the group: "));
        form.add(nameInput);

        HtmlTextField emailInput = new HtmlTextField(email, Context.tr("Email of the group: "));
        form.add(emailInput);

        HtmlSimpleDropDown rightInput = new HtmlSimpleDropDown(right, Context.tr("Type of the group : "));
        rightInput.add(Context.tr("Public"), CreateTeamAction.PUBLIC);
        rightInput.add(Context.tr("Protected"), CreateTeamAction.PROTECTED);
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
