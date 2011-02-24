package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.model.UserContent;
import com.bloatit.model.admin.UserContentAdmin;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class UserContentAdminPageImplementation extends
        UserContentAdminPage<DaoUserContent, UserContent<DaoUserContent>, UserContentAdminListFactory.DefaultFactory> {

    private final UserContentAdminPageUrl url;

    public UserContentAdminPageImplementation(final UserContentAdminPageUrl url) {
        super(url, new UserContentAdminListFactory.DefaultFactory());
        this.url = url;
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
    protected void addColumns(final HtmlGenericTableModel<UserContent<DaoUserContent>> tableModel) {
        UserContentAdminPageUrl clonedUrl = url.clone();

        addAsGroupColumn(tableModel, clonedUrl);
        addCreationDateColumn(tableModel, clonedUrl);
        addNbFilesColumn(tableModel);
        addIsDeletedColumn(tableModel, clonedUrl);
        addTypeColumn(tableModel);
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {
        addIsDeletedFilter(form, url);
        addHasFileFilter(form, url);
        addAsGroupFilter(form, url);
    }

}
