package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoKudosable;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.model.Kudosable;
import com.bloatit.model.KudosableInterface;
import com.bloatit.model.admin.KudosableAdmin;
import com.bloatit.model.admin.KudosableAdminListFactory;
import com.bloatit.web.actions.AdministrationAction;
import com.bloatit.web.url.KudosableAdminPageUrl;

@ParamContainer("admin/kudosable")
public abstract class KudosableAdminPage<T extends DaoKudosable, U extends KudosableInterface<T>, V extends KudosableAdminListFactory<T, U>> extends
        UserContentAdminPage<T, U, V> {

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("false")
    private final Boolean orderByPopularity;

    @RequestParam(role = RequestParam.Role.POST)
    private final Integer popularity;

    @RequestParam(role = RequestParam.Role.POST)
    private final DisplayableComparator popularityComparator;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterLoked;

    @RequestParam(role = RequestParam.Role.POST)
    private final DisplayableState popularityState;

    private final KudosableAdminPageUrl url;

    public KudosableAdminPage(final KudosableAdminPageUrl url, final V factory) {
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
                } catch (UnauthorizedOperationException e) {
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

    protected void addPopularityStateFilter(final HtmlForm form) {
        final HtmlDropDown state = new HtmlDropDown(url.getPopularityStateParameter().formFieldData());
        state.addDropDownElements(EnumSet.allOf(DisplayableState.class));
        state.setLabel(tr("Filter by Popularity State"));

        form.add(state);
    }

    protected void addPopularityFilter(final HtmlForm form) {
        final HtmlTextField popularity = new HtmlTextField(url.getPopularityParameter().getName(), tr("popularity"));
        popularity.setDefaultValue(url.getPopularityParameter().getStringValue());

        final HtmlDropDown comparator = new HtmlDropDown(url.getPopularityComparatorParameter().formFieldData());
        comparator.addDropDownElements(EnumSet.allOf(DisplayableComparator.class));
        form.add(popularity);
        form.add(comparator);
    }

    protected abstract void doAddActions(HtmlDropDown dropDown, HtmlBranch block);

}
