package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.model.admin.UserContentAdmin;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class UserContentAdminPageImplementation extends
        UserContentAdminPage<DaoUserContent, UserContentAdmin<DaoUserContent>, UserContentAdminListFactory.DefaultFactory> {

    public UserContentAdminPageImplementation(final UserContentAdminPageUrl url) {
        super(url, new UserContentAdminListFactory.DefaultFactory());
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
    protected void addColumns(final HtmlGenericTableModel<UserContentAdmin<DaoUserContent>> tableModel) {
        // Nothing to do here.
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {
        // Nothing to do here.
    }

}
