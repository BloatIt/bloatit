package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.model.admin.UserContentAdmin;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class UserContentAdminPageImplementation
        extends
        UserContentAdminPage<DaoUserContent, UserContentAdmin<DaoUserContent>, UserContentAdminListFactory<DaoUserContent, UserContentAdmin<DaoUserContent>>> {

    public UserContentAdminPageImplementation(UserContentAdminPageUrl url) {
        super(url, new UserContentAdminListFactory<DaoUserContent, UserContentAdmin<DaoUserContent>>());
    }

    @Override
    protected String getPageTitle() {
        return tr("Administration UserContent");
    }

    @Override
    public final boolean isStable() {
        return true;
    }

    @Override
    protected void addColumns(HtmlGenericTableModel<UserContentAdmin<DaoUserContent>> tableModel) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void addFormFilters(HtmlForm form) {
        // TODO Auto-generated method stub

    }

}
