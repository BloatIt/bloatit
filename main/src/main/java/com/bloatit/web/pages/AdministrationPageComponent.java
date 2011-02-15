package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormBlock;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlRadioButton;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.model.UserContentAdministrationListFactory;
import com.bloatit.web.url.AdministrationPageUrl;

public class AdministrationPageComponent<T extends DaoUserContent> {

    private AdministrationTableModel<T> tableModel;
    private UserContentAdministrationListFactory<T> factory;
    private PlaceHolderElement everything;

    public AdministrationPageComponent(AdministrationTableModel<T> tableModel) {
        super();
        this.tableModel = tableModel;
        factory = tableModel.getFactory();
    }

    public void generateArray(boolean orderByAsGroupAsc,
                              boolean orderByAsGroupDesc,
                              boolean orderByMemberAsc,
                              boolean orderByMemberDesc,
                              boolean deletedOnly,
                              boolean nonDeletedOnly,
                              boolean withoutFile,
                              boolean withFile,
                              boolean withAnyGroup,
                              boolean withNoGroup) throws RedirectException {

        everything = new PlaceHolderElement();
        if (Context.getSession().getAuthToken().getMember().getRole() != com.bloatit.data.DaoMember.Role.ADMIN) {
            Context.getSession().notifyError(tr("You have to be the administrator to access this page."));
            throw new RedirectException(new PageNotFoundUrl());
        }

        if (orderByAsGroupAsc) {
            factory.orderByAsGroup(OrderType.ASC);
        }
        if (orderByAsGroupDesc) {
            factory.orderByAsGroup(OrderType.DESC);
        }
        if (orderByMemberAsc) {
            factory.orderByMember(OrderType.ASC);
        }
        if (orderByMemberDesc) {
            factory.orderByMember(OrderType.DESC);
        }
        if (deletedOnly) {
            factory.deletedOnly();
        }
        if (nonDeletedOnly) {
            factory.nonDeletedOnly();
        }
        if (withoutFile) {
            factory.withoutFile();
        }
        if (withFile) {
            factory.withFile();
        }
        if (withAnyGroup) {
            factory.withAnyGroup();
        }
        if (withNoGroup) {
            factory.withNoGroup();
        }

        HtmlTable htmlTable = new HtmlTable(tableModel);

        everything.add(htmlTable);
    }

    public HtmlElement getComponent() {
        return everything;
    }

    public void generateFilterForm() {
        AdministrationPageUrl url = new AdministrationPageUrl();
        HtmlForm form = new HtmlForm(url.urlString());
        everything.add(form);

        // order by
        HtmlRadioButtonGroup groupOrder = new HtmlRadioButtonGroup("order");
        form.add(new HtmlFormBlock("order by").add(groupOrder));
        generateFormOrder(url, groupOrder);

        // delete ?
        HtmlRadioButtonGroup groupDeleted = new HtmlRadioButtonGroup("deleted");
        form.add(new HtmlFormBlock("Deleted ?").add(groupDeleted));
        groupDeleted.add(new HtmlRadioButton(url.getDeletedOnlyParameter().createFormFieldData(),
                                             tr("show only deleted content"),
                                             LabelPosition.BEFORE));
        groupDeleted.add(new HtmlRadioButton(url.getNonDeletedOnlyParameter().createFormFieldData(),
                                             tr("show non deleted content"),
                                             LabelPosition.BEFORE));

        // vrac
        form.add(new HtmlCheckbox(url.getWithoutFileParameter().createFormFieldData(),
                                  tr("show content without file associated"),
                                  LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithFileParameter().createFormFieldData(), tr("show content with file associated"), LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithAnyGroupParameter().createFormFieldData(),
                                  tr("show content created in the name of a group"),
                                  LabelPosition.BEFORE));
        form.add(new HtmlCheckbox(url.getWithNoGroupParameter().createFormFieldData(),
                                  tr("show content created in the name of a member"),
                                  LabelPosition.BEFORE));
        form.add(new HtmlSubmit(tr("Filter")));
    }

    protected void generateFormOrder(AdministrationPageUrl url, HtmlRadioButtonGroup groupOrder) {
        groupOrder.add(new HtmlRadioButton(url.getOrderByMemberAscParameter().createFormFieldData(), tr("member asc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByMemberDescParameter().createFormFieldData(), tr("member desc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByAsGroupAscParameter().createFormFieldData(), tr("group asc"), LabelPosition.BEFORE));
        groupOrder.add(new HtmlRadioButton(url.getOrderByAsGroupDescParameter().createFormFieldData(), tr("group desc"), LabelPosition.BEFORE));
    }
}
