package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.UserContentAdministrationListFactory;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("administration")
public class AdministrationPage extends LoggedPage {

    public enum FilterType implements HtmlRadioButtonGroup.Displayable {
        NO_FILTER(tr("No filter")), //
        WITH(tr("Yes")), //
        WITHOUT(tr("No"));

        private String displayName;

        @Override
        public String getDisplayName() {
            return displayName;
        }

        private FilterType(String displayName) {
            this.displayName = displayName;
        }

    }

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private String orderBy;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean asc;

    @RequestParam(level = Level.ERROR, defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private FilterType filterDeleted;

    @RequestParam(level = Level.ERROR, defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private FilterType filterFile;

    @RequestParam(level = Level.ERROR, defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private FilterType filterGroup;

    public AdministrationPage(AdministrationPageUrl url) {
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

        AdministrationPageComponent<DaoUserContent> administrationPageComponent = new AdministrationPageComponent<DaoUserContent>(new AdministrationTableModel<DaoUserContent>(new UserContentAdministrationListFactory<DaoUserContent>()));
        administrationPageComponent.generateFilterForm();
        administrationPageComponent.filter(orderBy, asc, filterDeleted, filterFile, filterGroup);
        administrationPageComponent.generateTable();
        administrationPageComponent.generateActionForm();

        return administrationPageComponent.getComponent();
    }

    @Override
    public String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }

}
