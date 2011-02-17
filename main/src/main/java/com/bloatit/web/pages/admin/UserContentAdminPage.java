package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
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
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.admin.UserContentAdmin;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/usercontent")
public abstract class UserContentAdminPage<U extends DaoUserContent, V extends UserContentAdmin<U>, T extends UserContentAdminListFactory<U, V>>
        extends LoggedPage {

    public enum OrderByUserContent implements HtmlRadioButtonGroup.Displayable {
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

        private OrderByUserContent(String displayName) {
            this.displayName = displayName;
        }
    }

    @RequestParam(defaultValue = "creationDate")
    private String orderByStr;
    
    @RequestParam(defaultValue = "DATE", role = RequestParam.Role.POST)
    private OrderByUserContent orderBy;
    
    @RequestParam(defaultValue = "false", role = RequestParam.Role.POST)
    private Boolean asc;
    
    @RequestParam(defaultValue = "WITHOUT", role = RequestParam.Role.POST)
    private DisplayableFilterType filterDeleted;
    
    @RequestParam(defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private DisplayableFilterType filterFile;
    
    @RequestParam(defaultValue = "NO_FILTER", role = RequestParam.Role.POST)
    private DisplayableFilterType filterGroup;
    
    private T factory;
    private final UserContentAdminPageUrl url;

    protected UserContentAdminPage(UserContentAdminPageUrl url,  T factory) {
        super(url);
        this.url = url;
        this.factory = factory;
        orderByStr = url.getOrderByStr();
        orderBy = url.getOrderBy();
        asc = url.getAsc();
        filterDeleted = url.getFilterDeleted();
        filterFile = url.getFilterFile();
        filterGroup = url.getFilterGroup();

        // Save parameters
        Context.getSession().addParameter(url.getOrderByStrParameter());
        Context.getSession().addParameter(url.getOrderByParameter());
        Context.getSession().addParameter(url.getAscParameter());
        Context.getSession().addParameter(url.getFilterDeletedParameter());
        Context.getSession().addParameter(url.getFilterFileParameter());
        Context.getSession().addParameter(url.getFilterGroupParameter());
    }
    
    @Override
    public final HtmlElement createRestrictedContent() throws RedirectException {
        PlaceHolderElement everything = new PlaceHolderElement();

        // Filter form private HtmlForm filterForm;
        HtmlForm filterForm = new HtmlForm(url.urlString());
        everything.add(filterForm);
        generateFilterForm(filterForm);

        // Apply filters
        filter(orderBy, asc, filterDeleted, filterFile, filterGroup);

        // Action form
        AdministrationActionUrl actionUrl = new AdministrationActionUrl();
        HtmlForm actionForm = new HtmlForm(actionUrl.urlString());
        everything.add(actionForm);
        generateActionForm(actionForm);
        generateTable(actionForm);

        return everything;
    }

    @Override
    public final String getRefusalReason() {
        return tr("You have to be the administrator to access this page.");
    }

    public final void generateFilterForm(HtmlForm filterForm) {
        UserContentAdminPageUrl url = new UserContentAdminPageUrl();
        // order by
        HtmlDropDown order = new HtmlDropDown(url.getOrderByParameter().formFieldData());
        HtmlFormBlock blockOrder = new HtmlFormBlock("Order by");
        filterForm.add(blockOrder.add(order));
        order.addDropDownElements(EnumSet.allOf(OrderByUserContent.class));
        addFormOrder(order);
        blockOrder.add(new HtmlCheckbox(url.getAscParameter().formFieldData(), tr("Asc"), LabelPosition.BEFORE));

        // delete ?
        HtmlDropDown groupDeleted = new HtmlDropDown(url.getFilterDeletedParameter().formFieldData());
        groupDeleted.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupDeleted.setLabel(tr("Filter by deleted content"));
        filterForm.add(groupDeleted);

        // Files
        HtmlDropDown groupFile = new HtmlDropDown(url.getFilterFileParameter().formFieldData());
        groupFile.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupFile.setLabel(tr("Filter by Content with file"));
        filterForm.add(groupFile);
        
        HtmlDropDown groupAsGroup = new HtmlDropDown(url.getFilterGroupParameter().formFieldData());
        groupAsGroup.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupAsGroup.setLabel(tr("Filter by Content created as a group"));
        filterForm.add(groupAsGroup);

        // extends
        addFormFilters(filterForm);

        // submit
        filterForm.add(new HtmlSubmit(tr("Filter")));
    }

    public final void filter(OrderByUserContent orderBy, boolean asc, DisplayableFilterType filterDeleted, DisplayableFilterType filterFile, DisplayableFilterType filterGroup) {

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

        if (filterDeleted == DisplayableFilterType.WITH) {
            factory.deletedOnly();
        } else if (filterDeleted == DisplayableFilterType.WITHOUT) {
            factory.nonDeletedOnly();
        }
        if (filterFile == DisplayableFilterType.WITH) {
            factory.withoutFile();
        } else if (filterFile == DisplayableFilterType.WITHOUT) {
            factory.withFile();
        }
        if (filterGroup == DisplayableFilterType.WITH) {
            factory.withAnyGroup();
        } else if (filterGroup == DisplayableFilterType.WITHOUT) {
            factory.withNoGroup();
        }
    }

    public final void generateTable(HtmlForm actionForm) {
        HtmlGenericTableModel<V> tableModel = new HtmlGenericTableModel<V>(factory.list());

        tableModel.addColumn(new HtmlCheckbox("id_all", LabelPosition.BEFORE), new ColumnGenerator<V>() {
            @Override
            public HtmlNode getBody(V element) {
                HtmlCheckbox htmlCheckbox = new HtmlCheckbox("id", LabelPosition.BEFORE);
                htmlCheckbox.addAttribute("value", element.getId().toString());
                return htmlCheckbox;
            }
        });

        url.setOrderByStr("member");
        tableModel.addColumn(url.getHtmlLink(tr("Author")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return element.getAuthor();
            }
        });

        url.setOrderByStr("asGroup");
        tableModel.addColumn(url.getHtmlLink(tr("asGroup")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return element.getAsGroup();
            }
        });

        url.setOrderByStr("creationDate");
        tableModel.addColumn(url.getHtmlLink(tr("Creation date")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return Context.getLocalizator().getDate(element.getCreationDate()).toString(FormatStyle.MEDIUM);
            }
        });

        tableModel.addColumn(tr("Nb files"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return String.valueOf(element.getFilesNumber());
            }
        });

        url.setOrderByStr("isDeleted");
        tableModel.addColumn(url.getHtmlLink(tr("Deleted")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return String.valueOf(element.isDeleted());
            }
        });

        tableModel.addColumn(tr("Type"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(V element) {
                return element.getType();
            }
        });

        addColumns(tableModel);
        actionForm.add(new HtmlTable(tableModel));
    }

    public final void generateActionForm(HtmlForm actionForm) {
        // add the action drop down
        HtmlRadioButtonGroup group = new HtmlRadioButtonGroup("UserContent_actions");
        HtmlFormBlock block = new HtmlFormBlock("UserContent_actions");
        actionForm.add(block.add(group));
        HtmlDropDown dropDown = new HtmlDropDown("action");
        group.add(dropDown);
        addActions(dropDown, block);

        // add the submit button
        actionForm.add(new HtmlSubmit(tr("Validate")));
    }

    protected void addActions(HtmlDropDown dropDown, HtmlBranch actionGroup) {
        // redefine me in subclasses.
        dropDown.addDropDownElements(new AdminActionManager().userContentActions());
    }

    protected abstract void addColumns(HtmlGenericTableModel<V> tableModel);

    protected abstract void addFormOrder(HtmlDropDown order);

    protected abstract void addFormFilters(HtmlForm form);

    protected T getFactory() {
        return factory;
    }

}