package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.model.UserContent;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
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
    public void processErrors() throws RedirectException {
        session.notifyList(url.getMessages());
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<UserContent<DaoUserContent>> tableModel) {
        final UserContentAdminPageUrl clonedUrl = url.clone();

        addAsTeamColumn(tableModel, clonedUrl);
        addCreationDateColumn(tableModel, clonedUrl);
        addNbFilesColumn(tableModel);
        addIsDeletedColumn(tableModel, clonedUrl);
        addTypeColumn(tableModel);
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {
        addIsDeletedFilter(form, url);
        addHasFileFilter(form, url);
        addAsTeamFilter(form, url);
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return UserContentAdminPageImplementation.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new UserContentAdminPageUrl().getHtmlLink(tr("User content administration")));

        return breadcrumb;
    }

}
