package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel.ColumnGenerator;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.model.admin.IdentifiableAdminListFactory;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.IdentifiablesAdminPageUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("IdentifiablesAdminPage")
public abstract class IdentifiablesAdminPage<U extends DaoIdentifiable, V extends IdentifiableInterface, T extends IdentifiableAdminListFactory<U, V>>
        extends AdminPage {

    @RequestParam
    @Optional("id")
    private final String orderByStr;

    @RequestParam
    @Optional("false")
    private final Boolean asc;

    private final T factory;
    private final IdentifiablesAdminPageUrl url;

    protected IdentifiablesAdminPage(final IdentifiablesAdminPageUrl url, final T factory) {
        super(url);
        this.url = url;
        this.factory = factory;
        orderByStr = url.getOrderByStr();
        asc = url.getAsc();
    }

    @Override
    public final HtmlElement createAdminContent() {
        final PlaceHolderElement everything = new PlaceHolderElement();

        // Filter form private HtmlForm filterForm;
        final HtmlForm filterForm = new HtmlForm(url.urlString());
        everything.add(filterForm);
        generateFilterForm(filterForm);

        // Apply filters
        if (orderByStr != null && !orderByStr.isEmpty()) {
            factory.orderBy(orderByStr, asc ? OrderType.ASC : OrderType.DESC);
        }

        // Action form
        final AdministrationActionUrl actionUrl = new AdministrationActionUrl();
        final HtmlForm actionForm = new HtmlForm(actionUrl.urlString());
        everything.add(actionForm);
        generateActionForm(actionForm);
        generateTable(actionForm);

        return everything;
    }

    public final void generateFilterForm(final HtmlForm filterForm) {
        final UserContentAdminPageUrl url = new UserContentAdminPageUrl();
        // order by
        final HtmlFormBlock blockOrder = new HtmlFormBlock("Order by");
        filterForm.add(blockOrder);
        blockOrder.add(new HtmlCheckbox(url.getAscParameter().pickFieldData().getName(), tr("Asc"), LabelPosition.BEFORE));

        // extends
        addFormFilters(filterForm);

        // submit
        filterForm.add(new HtmlSubmit(tr("Filter")));
    }

    public final void generateTable(final HtmlForm actionForm) {
        final HtmlGenericTableModel<V> tableModel = new HtmlGenericTableModel<V>(factory.list());

        tableModel.addColumn(new HtmlCheckbox("id_all", LabelPosition.BEFORE), new ColumnGenerator<V>() {
            @Override
            public XmlNode getBody(final V element) {
                final HtmlCheckbox htmlCheckbox = new HtmlCheckbox("id", LabelPosition.BEFORE);
                htmlCheckbox.addAttribute("value", element.getId().toString());
                return htmlCheckbox;
            }
        });

        tableModel.addColumn(tr("id"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return String.valueOf(element.getId());
            }
        });

        addColumns(tableModel);
        actionForm.add(new HtmlTable(tableModel));
    }

    protected void addTypeColumn(final HtmlGenericTableModel<V> tableModel) {
        tableModel.addColumn(tr("Type"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return element.getClass().getSimpleName();
            }
        });
    }

    public final void generateActionForm(final HtmlForm actionForm) {
        // add the action drop down
        final HtmlRadioButtonGroup group = new HtmlRadioButtonGroup("actions");
        final HtmlFormBlock block = new HtmlFormBlock("actions");
        actionForm.add(block.add(group));
        final HtmlDropDown dropDown = new HtmlDropDown("action");
        actionForm.add(dropDown);
        addActions(dropDown, block);

        // add the submit button
        actionForm.add(new HtmlSubmit(tr("Validate")));
    }

    protected abstract void addActions(final HtmlDropDown dropDown, final HtmlBranch actionGroup);

    protected abstract void addColumns(HtmlGenericTableModel<V> tableModel);

    protected abstract void addFormFilters(HtmlForm form);

    protected T getFactory() {
        return factory;
    }

}
