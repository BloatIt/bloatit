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

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.KudosableInterface;
import com.bloatit.model.admin.KudosableAdminListFactory;
import com.bloatit.web.url.KudosableAdminPageUrl;

@ParamContainer("admin/kudosable")
public abstract class KudosableAdminPage<T extends DaoKudosable, U extends KudosableInterface, V extends KudosableAdminListFactory<T, U>> extends
        UserContentAdminPage<T, U, V> {

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("false")
    private final Boolean orderByPopularity;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional
    private final Integer popularity;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("EQUAL")
    private final DisplayableComparator popularityComparator;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterLoked;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional
    private final DisplayableState popularityState;

    private final KudosableAdminPageUrl url;

    protected KudosableAdminPage(final KudosableAdminPageUrl url, final V factory) {
        super(url, factory);
        this.url = url;
        filterLoked = url.getFilterLoked();
        orderByPopularity = url.getOrderByPopularity();
        popularity = url.getPopularity();
        popularityComparator = url.getPopularityComparator();
        popularityState = url.getPopularityState();

        // Add some filters
        if (orderByPopularity) {
            getFactory().orderByPopularity(url.getAsc() ? OrderType.ASC : OrderType.DESC);
            Context.getSession().addParameter(url.getOrderByPopularityParameter());
        }
        if (popularity != null && popularityComparator != null) {
            getFactory().popularity(DisplayableComparator.getComparator(popularityComparator), popularity);
            Context.getSession().addParameter(url.getPopularityComparatorParameter());
            Context.getSession().addParameter(url.getPopularityParameter());
        }
        if (popularityState != null && popularityState != DisplayableState.NO_FILTER) {
            getFactory().stateEquals(DisplayableState.getState(popularityState));
            Context.getSession().addParameter(url.getPopularityStateParameter());
        }
        if (filterLoked == DisplayableFilterType.WITH) {
            getFactory().lokedOnly();
            Context.getSession().addParameter(url.getFilterLokedParameter());
        } else if (filterLoked == DisplayableFilterType.WITHOUT) {
            getFactory().nonLokedOnly();
            Context.getSession().addParameter(url.getFilterLokedParameter());
        }
    }

    protected void addIsLockedColumn(final HtmlGenericTableModel<U> tableModel, final KudosableAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("isPopularityLocked");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Is locked")), new StringColumnGenerator<U>() {
            @Override
            public String getStringBody(final U element) {
                try {
                    return String.valueOf(element.isPopularityLocked());
                } catch (final UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });
    }

    protected void addPopularityStateColumn(final HtmlGenericTableModel<U> tableModel, final KudosableAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("popularityState");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("State")), new StringColumnGenerator<U>() {
            @Override
            public String getStringBody(final U element) {
                return String.valueOf(element.getState());
            }
        });
    }

    protected void addPopularityColumn(final HtmlGenericTableModel<U> tableModel, final KudosableAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("popularity");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Popularity")), new StringColumnGenerator<U>() {
            @Override
            public String getStringBody(final U element) {
                return String.valueOf(element.getPopularity());
            }
        });
    }

    @Override
    protected final void addActions(final HtmlDropDown dropDown, final HtmlBranch group) {
        super.addActions(dropDown, group);
        dropDown.addDropDownElements(new AdminActionManager().kudosableActions());
        final HtmlDropDown stateSetter = new HtmlDropDown(AdministrationAction.POPULARITY_STATE_CODE, tr("Set the state"));
        stateSetter.addDropDownElements(EnumSet.allOf(DisplayableState.class));
        group.add(stateSetter);
        doAddActions(dropDown, group);
    }

    protected void addPopularityStateFilter(final HtmlBranch form) {
        final FieldData stateData = url.getPopularityStateParameter().pickFieldData();
        final HtmlDropDown state = new HtmlDropDown(stateData.getName());
        state.setDefaultValue(stateData.getSuggestedValue());
        state.addErrorMessages(stateData.getErrorMessages());
        state.addDropDownElements(EnumSet.allOf(DisplayableState.class));
        state.setLabel(tr("Filter by Popularity State"));

        form.add(state);
    }

    protected void addPopularityFilter(final HtmlBranch form) {
        final HtmlTextField popularity = new HtmlTextField(url.getPopularityParameter().getName(), tr("popularity"));
        popularity.setDefaultValue(url.getPopularityParameter().getStringValue());

        final FieldData comparatorData = url.getPopularityComparatorParameter().pickFieldData();
        final HtmlDropDown comparator = new HtmlDropDown(comparatorData.getName());
        comparator.setDefaultValue(comparatorData.getSuggestedValue());
        comparator.addErrorMessages(comparatorData.getErrorMessages());
        comparator.addDropDownElements(EnumSet.allOf(DisplayableComparator.class));
        form.add(popularity);
        form.add(comparator);
    }

    protected abstract void doAddActions(HtmlDropDown dropDown, HtmlBranch block);

}
