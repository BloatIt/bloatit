package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoKudosable;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.model.admin.KudosableAdmin;
import com.bloatit.model.admin.KudosableAdminListFactory;
import com.bloatit.web.url.KudosableAdminPageUrl;

public final class KudosableAdminPageImplementation extends
        KudosableAdminPage<DaoKudosable, KudosableAdmin<DaoKudosable>, KudosableAdminListFactory<DaoKudosable, KudosableAdmin<DaoKudosable>>> {

    public KudosableAdminPageImplementation(KudosableAdminPageUrl url) {
        super(url, new KudosableAdminListFactory<DaoKudosable, KudosableAdmin<DaoKudosable>>());
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
    protected void doAddColumns(HtmlGenericTableModel<KudosableAdmin<DaoKudosable>> tableModel) {
        // Everything done in super class
    }

    @Override
    protected void doAddActions(HtmlDropDown dropDown, HtmlBranch block) {
        // Everything done in super class
    }

    @Override
    protected void doAddFormOrder(HtmlDropDown order) {
        // Everything done in super class
    }

    @Override
    protected void doAddFormFilters(HtmlForm form) {
        // Everything done in super class
    }

}
