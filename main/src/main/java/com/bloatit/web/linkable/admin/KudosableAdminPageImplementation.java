package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoKudosable;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.model.Kudosable;
import com.bloatit.model.admin.KudosableAdminListFactory;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.KudosableAdminPageUrl;

public final class KudosableAdminPageImplementation extends
        KudosableAdminPage<DaoKudosable, Kudosable<DaoKudosable>, KudosableAdminListFactory<DaoKudosable, Kudosable<DaoKudosable>>> {

    private final KudosableAdminPageUrl url;

    public KudosableAdminPageImplementation(final KudosableAdminPageUrl url) {
        super(url, new KudosableAdminListFactory<DaoKudosable, Kudosable<DaoKudosable>>());
        this.url = url;
    }

    @Override
    protected String getPageTitle() {
        return tr("Administration Kudosable");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public void processErrors() throws RedirectException {
        session.notifyList(url.getMessages());
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<Kudosable<DaoKudosable>> tableModel) {
        KudosableAdminPageUrl clonedUrl = url.clone();

        addAsTeamColumn(tableModel, clonedUrl);
        addCreationDateColumn(tableModel, clonedUrl);
        addTypeColumn(tableModel);
        // addNbFilesColumn(tableModel);
        // addIsDeletedColumn(tableModel, clonedUrl);

        addPopularityColumn(tableModel, clonedUrl);
        addPopularityStateColumn(tableModel, clonedUrl);
        addIsLockedColumn(tableModel, clonedUrl);
    }

    @Override
    protected void doAddActions(final HtmlDropDown dropDown, final HtmlBranch block) {
        // Everything done in super class
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {
        addIsDeletedFilter(form, url);
        addHasFileFilter(form, url);
        addAsTeamFilter(form, url);
        addPopularityStateFilter(form);
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return KudosableAdminPageImplementation.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new KudosableAdminPageUrl().getHtmlLink(tr("Votable administration")));

        return breadcrumb;
    }

}
