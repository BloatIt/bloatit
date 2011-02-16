package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.UserContentAdministrationListFactory;
import com.bloatit.web.url.AdministrationPageUrl;

@ParamContainer("administration")
public class AdministrationPage extends LoggedPage {

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByMemberAsc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByAsGroupAsc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByMemberDesc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean orderByAsGroupDesc;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean deletedOnly;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean nonDeletedOnly;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withoutFile;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withFile;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withAnyGroup;

    @RequestParam(level = Level.ERROR, defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean withNoGroup;

    // @RequestParam(level = Level.INFO, role = RequestParam.Role.POST)
    // @ParamConstraint(optional = true)
    // private Member fromMember;
    // @RequestParam(level = Level.INFO, role = RequestParam.Role.POST)
    // @ParamConstraint(optional = true)
    // private Group fromGroup;

    public AdministrationPage(AdministrationPageUrl url) {
        super(url);
        orderByMemberAsc = url.getOrderByMemberAsc();
        orderByMemberDesc = url.getOrderByMemberDesc();
        orderByAsGroupAsc = url.getOrderByAsGroupAsc();
        orderByAsGroupDesc = url.getOrderByAsGroupDesc();
        deletedOnly = url.getDeletedOnly();
        nonDeletedOnly = url.getNonDeletedOnly();
        withoutFile = url.getWithoutFile();
        withFile = url.getWithFile();
        withAnyGroup = url.getWithAnyGroup();
        withNoGroup = url.getWithNoGroup();
        // fromMember = url.getFromMember();
        // fromGroup = url.getFromGroup();
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
        administrationPageComponent.generateArray(orderByAsGroupAsc,
                                                      orderByAsGroupDesc,
                                                      orderByMemberAsc,
                                                      orderByMemberDesc,
                                                      deletedOnly,
                                                      nonDeletedOnly,
                                                      withoutFile,
                                                      withFile,
                                                      withAnyGroup,
                                                      withNoGroup);

        continueGeneration(administrationPageComponent);

        return administrationPageComponent.getComponent();
    }

    protected void continueGeneration(AdministrationPageComponent<?> administrationPageComponent) {
        // implement me in sub class
    }

    @Override
    public String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }

}
