package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("administration")
public class AdministrationPage extends MasterPage{

    public AdministrationPage(AdministrationPageUrl url) {
        super(url);
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlForm form = new HtmlForm(new AdministrationActionUrl().urlString());

        HtmlCheckbox checkBox1 = new HtmlCheckbox("coucou", LabelPosition.AFTER);
        checkBox1.addAttribute("value", "plop");
        HtmlCheckbox checkBox2 = new HtmlCheckbox("coucou", LabelPosition.AFTER);
        checkBox2.addAttribute("value", "plip");
        form.add(checkBox1);
        form.add(checkBox2);
        form.add(new HtmlSubmit("test"));
        add(form);
    }

    @Override
    protected String getPageTitle() {
        return "test";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
