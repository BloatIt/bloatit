package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
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
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.admin.UserContentAdministration;
import com.bloatit.model.admin.UserContentAdministrationListFactory;
import com.bloatit.web.pages.admin.UserContentAdminPage.UserContentOrderBy;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

public class UserContentAdminPageComponent<T extends DaoUserContent> {

    private UserContentAdministrationListFactory<T> factory;

    private PlaceHolderElement everything;
    private HtmlForm filterForm;
    private HtmlForm actionForm;

    public UserContentAdminPageComponent() {
        this(new UserContentAdministrationListFactory<T>());
    }

    protected UserContentAdminPageComponent(UserContentAdministrationListFactory<T> factory) {
        super();
        this.factory = factory;
        everything = new PlaceHolderElement();

        // Filter form
        UserContentAdminPageUrl url = new UserContentAdminPageUrl();
        filterForm = new HtmlForm(url.urlString());
        everything.add(filterForm);

        // Action form
        AdministrationActionUrl actionUrl = new AdministrationActionUrl();
        actionForm = new HtmlForm(actionUrl.urlString());

        everything.add(actionForm);
    }

    public final void generateFilterForm() {
        UserContentAdminPageUrl url = new UserContentAdminPageUrl();
        // order by
        HtmlDropDown order = new HtmlDropDown(url.getOrderByParameter().formFieldData());
        filterForm.add(new HtmlFormBlock("Order by").add(order));
        order.addDropDownElements(EnumSet.allOf(UserContentOrderBy.class));
        addFormOrder(order);

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

    public final void filter(UserContentOrderBy orderBy, boolean asc, FilterType filterDeleted, FilterType filterFile, FilterType filterGroup) {
        
        switch (orderBy) {
        case DATE:
            factory.orderByCreationDate(asc ? OrderType.ASC : OrderType.DESC);
            break;
        case GROUP:
            factory.orderByAsGroup(asc ? OrderType.ASC : OrderType.DESC);
            break;
        case MEMBER:
            factory.orderByMember(asc ? OrderType.ASC : OrderType.DESC);
            break;
        case NOTHING:
            break;
        default:
            break;
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

        tableModel.addColumn(tr("Creation date"), new StringColumnGenerator<UserContentAdministration<T>>() {
            @Override
            public String getStringBody(UserContentAdministration<T> element) {
                return Context.getLocalizator().getDate(element.getCreationDate()).toString(FormatStyle.MEDIUM);
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
        addActions(dropDown);

        // add the submit button
        actionForm.add(new HtmlSubmit(tr("Validate")));
    }

    public final HtmlElement getComponent() {
        return everything;
    }

    protected void addColumns(HtmlGenericTableModel<UserContentAdministration<T>> tableModel) {
        // Implement me in sub classes
    }

    protected void addActions(HtmlDropDown dropDown) {
        // redefine me in subclasses.
        dropDown.addDropDownElements(new AdminActionManager().userContentActions());
    }

    protected void addFormOrder(HtmlDropDown order) {
        // Implement me in sub classes
    }

    protected void addFormFilters(HtmlForm form) {
        // Implement me in sub classes
    }
}
