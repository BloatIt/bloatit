package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/usercontent")
public class UserContentAdminPage extends LoggedPage {

    public enum UserContentOrderBy implements HtmlRadioButtonGroup.Displayable {
        NOTHING(tr("No order")), //
        MEMBER(tr("Member")), //
        GROUP(tr("Group")), //
        DATE(tr("Creation date")), //
        TYPE(tr("Type"));

        private String displayName;

        @Override
        public String getDisplayName() {
            return displayName;
        }

        private UserContentOrderBy(String displayName) {
            this.displayName = displayName;
        }
    }

    @RequestParam(level = Level.ERROR, defaultValue = "DATE", role = RequestParam.Role.POST)
    private UserContentOrderBy orderBy;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean asc;

    @RequestParam(level = Level.ERROR, defaultValue = "WITHOUT", role = RequestParam.Role.POST)
    private FilterType filterDeleted;

    @RequestParam(level = Level.ERROR, defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private FilterType filterFile;

    @RequestParam(level = Level.ERROR, defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private FilterType filterGroup;

    public UserContentAdminPage(UserContentAdminPageUrl url) {
        super(url);
        orderBy = url.getOrderBy();
        asc = url.getAsc();
        filterDeleted = url.getFilterDeleted();
        filterFile = url.getFilterFile();
        filterGroup = url.getFilterGroup();

        // Save parameters
        Context.getSession().addParameter(url.getFilterDeletedParameter());
        Context.getSession().addParameter(url.getFilterFileParameter());
        Context.getSession().addParameter(url.getFilterGroupParameter());
    }

    @Override
    protected String getPageTitle() {
        return tr("Administration UserContent");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    public HtmlElement createRestrictedContent() throws RedirectException {

        UserContentAdminPageComponent<DaoUserContent> component = new UserContentAdminPageComponent<DaoUserContent>();
        component.generateFilterForm();
        component.filter(orderBy, asc, filterDeleted, filterFile, filterGroup);
        component.generateTable();
        component.generateActionForm();

        return component.getComponent();
    }

    @Override
    public String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }

}
