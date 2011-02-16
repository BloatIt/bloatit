package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;
import java.util.List;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.ColumnGenerator;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormBlock;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.SimpleDropDownElement;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.UserContentAdministration;
import com.bloatit.model.UserContentAdministrationListFactory;
import com.bloatit.web.pages.AdministrationPage.FilterType;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.AdministrationPageUrl;

public class AdministrationPageComponent<T extends DaoUserContent> {

    private UserContentAdministrationListFactory<T> factory;

    private PlaceHolderElement everything;
    private HtmlForm filterForm;
    private HtmlForm actionForm;

    public AdministrationPageComponent(AdministrationTableModel<T> tableModel1) {
        super();
        factory = tableModel1.getFactory();
        everything = new PlaceHolderElement();

        // Filter form
        AdministrationPageUrl url = new AdministrationPageUrl();
        filterForm = new HtmlForm(url.urlString());
        everything.add(filterForm);

        // Action form
        AdministrationActionUrl actionUrl = new AdministrationActionUrl();
        actionForm = new HtmlForm(actionUrl.urlString());

        everything.add(actionForm);
    }

    public final void generateFilterForm() {
        AdministrationPageUrl url = new AdministrationPageUrl();
        // order by
        HtmlRadioButtonGroup groupOrder = new HtmlRadioButtonGroup("order");
        filterForm.add(new HtmlFormBlock("Order by").add(groupOrder));
        // groupOrder.add(new
        // HtmlRadioButton(url.getOrderByMemberAscParameter().createFormFieldData(),
        // tr("member asc"), LabelPosition.BEFORE));
        // groupOrder.add(new
        // HtmlRadioButton(url.getOrderByMemberDescParameter().createFormFieldData(),
        // tr("member desc"), LabelPosition.BEFORE));
        // groupOrder.add(new
        // HtmlRadioButton(url.getOrderByAsGroupAscParameter().createFormFieldData(),
        // tr("group asc"), LabelPosition.BEFORE));
        // groupOrder.add(new
        // HtmlRadioButton(url.getOrderByAsGroupDescParameter().createFormFieldData(),
        // tr("group desc"), LabelPosition.BEFORE));
        addFormOrder(groupOrder);

        // delete ?
        HtmlRadioButtonGroup groupDeleted = new HtmlRadioButtonGroup(url.getFilterDeletedParameter().formFieldData());
        filterForm.add(new HtmlFormBlock("Only delete content").add(groupDeleted));
        groupDeleted.addRadioButton(EnumSet.allOf(FilterType.class));

        // Files
        HtmlRadioButtonGroup groupFile = new HtmlRadioButtonGroup(url.getFilterFileParameter().formFieldData());
        filterForm.add(new HtmlFormBlock("Content with file").add(groupFile));
        groupFile.addRadioButton(EnumSet.allOf(FilterType.class));

        HtmlRadioButtonGroup groupAsGroup = new HtmlRadioButtonGroup(url.getFilterGroupParameter().formFieldData());
        filterForm.add(new HtmlFormBlock("Content created as a group").add(groupAsGroup));
        groupAsGroup.addRadioButton(EnumSet.allOf(FilterType.class));

        // extends
        addFormFilters(filterForm);

        // submit
        filterForm.add(new HtmlSubmit(tr("Filter")));
    }

    public final void filter(String orderBy, boolean asc, FilterType filterDeleted, FilterType filterFile, FilterType filterGroup) {
        if (orderBy.equals("group")) {
            factory.orderByAsGroup(asc ? OrderType.ASC : OrderType.DESC);
        } else if (orderBy.equals("member")) {
            factory.orderByMember(asc ? OrderType.ASC : OrderType.DESC);
        }

        if (filterDeleted == FilterType.WITH) {
            factory.deletedOnly();
        } else if (filterDeleted == FilterType.WITHOUT) {
            factory.nonDeletedOnly();
        }
        if (filterFile == FilterType.WITH) {
            factory.withoutFile();
        } else if (filterFile == FilterType.WITHOUT) {
            factory.withFile();
        }
        if (filterGroup == FilterType.WITH) {
            factory.withAnyGroup();
        } else if (filterGroup == FilterType.WITHOUT) {
            factory.withNoGroup();
        }
    }

    public final void generateTable() {
        HtmlGenericTableModel<UserContentAdministration<T>> tableModel = new HtmlGenericTableModel<UserContentAdministration<T>>(factory.ListUserContents());

        tableModel.addColumn(new HtmlCheckbox("id_all", LabelPosition.BEFORE), new ColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public HtmlNode getBody(UserContentAdministration<T> element) {
                HtmlCheckbox htmlCheckbox = new HtmlCheckbox("id", LabelPosition.BEFORE);
                htmlCheckbox.addAttribute("value", element.getId().toString());
                return htmlCheckbox;
            }
        });

        tableModel.addColumn(tr("Author"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return element.getAuthor();
            }
        });

        tableModel.addColumn(tr("asGroup"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return element.getAsGroup();
            }
        });

        tableModel.addColumn(tr("Nb files"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return String.valueOf(element.getFilesNumber());
            }
        });

        tableModel.addColumn(tr("Deleted"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return String.valueOf(element.isDeleted());
            }
        });

        tableModel.addColumn(tr("Type"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return element.getType();
            }
        });

        addColumns(tableModel);
        actionForm.add(new HtmlTable(tableModel));
    }

    public final void generateActionForm() {
        // add the action drop down
        HtmlRadioButtonGroup group = new HtmlRadioButtonGroup("UserContent_actions");
        actionForm.add(new HtmlFormBlock("UserContent_actions").add(group));
        HtmlDropDown dropDown = new HtmlDropDown("action");
        group.add(dropDown);
        for (SimpleDropDownElement simpleDropDownElement : getActions()) {
            dropDown.addDropDownElement(simpleDropDownElement.getCode(), simpleDropDownElement.getName());
        }
        addActions(dropDown);

        // add the submit button
        actionForm.add(new HtmlSubmit(tr("Validate")));
    }

    public final HtmlElement getComponent() {
        return everything;
    }

    protected List<SimpleDropDownElement> getActions() {
        // redefine me in subclasses.
        return AdministrationActionManager.userContentActions();
    }

    protected void addColumns(HtmlGenericTableModel<UserContentAdministration<T>> tableModel) {
        // Implement me in sub classes
    }

    protected void addActions(HtmlDropDown dropDown) {
        // Implement me in sub classes
    }

    protected void addFormOrder(HtmlRadioButtonGroup group) {
        // Implement me in sub classes
    }

    protected void addFormFilters(HtmlForm form) {
        // Implement me in sub classes
    }
}
