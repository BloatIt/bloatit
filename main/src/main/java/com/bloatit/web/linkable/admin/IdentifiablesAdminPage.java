//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoIdentifiable;
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
import com.bloatit.model.IdentifiableInterface;
import com.bloatit.model.admin.IdentifiableAdminListFactory;
import com.bloatit.web.linkable.admin.master.AdminPage;
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
        asc = url.getAsc().booleanValue();
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

    private final void generateFilterForm(final HtmlForm filterForm) {
        final UserContentAdminPageUrl url = new UserContentAdminPageUrl();
        final HtmlFormBlock block = new HtmlFormBlock("Filters");
        filterForm.add(block);
        // extends
        addFormFilters(block);

        // order by
        block.add(new HtmlCheckbox(url.getAscParameter().pickFieldData().getName(), tr("Asc"), LabelPosition.BEFORE));
        
        // submit
        block.add(new HtmlSubmit(tr("Filter")));
    }

    private final void generateTable(final HtmlForm actionForm) {
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

    private final void generateActionForm(final HtmlForm actionForm) {
        // add the action drop down
        final HtmlRadioButtonGroup group = new HtmlRadioButtonGroup("actions");
        final HtmlFormBlock block = new HtmlFormBlock("actions");
        actionForm.add(block);
        block.add(group);
        final HtmlDropDown dropDown = new HtmlDropDown("action");
        block.add(dropDown);
        addActions(dropDown, block);

        // add the submit button
        block.add(new HtmlSubmit(tr("Validate")));
    }

    protected abstract void addActions(final HtmlDropDown dropDown, final HtmlBranch actionGroup);

    protected abstract void addColumns(HtmlGenericTableModel<V> tableModel);

    protected abstract void addFormFilters(HtmlBranch form);

    protected T getFactory() {
        return factory;
    }

}
