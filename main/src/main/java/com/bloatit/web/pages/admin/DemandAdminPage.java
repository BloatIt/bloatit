package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoDemand;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.model.Demand;
import com.bloatit.model.admin.DemandAdminListFactory;
import com.bloatit.web.actions.AdministrationAction;
import com.bloatit.web.url.DemandAdminPageUrl;

@ParamContainer("admin/demands")
public final class DemandAdminPage extends KudosableAdminPage<DaoDemand, Demand, DemandAdminListFactory> {

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableDemandState filterByState;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterSelectedOffer;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterHasOffer;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterHasContribution;

    private final DemandAdminPageUrl url;

    public DemandAdminPage(final DemandAdminPageUrl url) {
        super(url, new DemandAdminListFactory());
        this.url = url;
        filterByState = url.getFilterByState();
        filterSelectedOffer = url.getFilterSelectedOffer();
        filterHasOffer = url.getFilterHasOffer();
        filterHasContribution = url.getFilterHasContribution();

        session.addParameter(url.getFilterByStateParameter());
        session.addParameter(url.getFilterSelectedOfferParameter());
        session.addParameter(url.getFilterHasOfferParameter());
        session.addParameter(url.getFilterHasContributionParameter());

        // Add some filters
        if (filterByState != null && filterByState != DisplayableDemandState.NO_FILTER) {
            getFactory().stateEquals(DisplayableDemandState.getDemandState(filterByState));
        }
        if (filterSelectedOffer == DisplayableFilterType.WITH) {
            getFactory().withSelectedOffer();
        } else if (filterSelectedOffer == DisplayableFilterType.WITHOUT) {
            getFactory().withoutSelectedOffer();
        }
        if (filterHasOffer == DisplayableFilterType.WITH) {
            getFactory().withOffer();
        } else if (filterHasOffer == DisplayableFilterType.WITHOUT) {
            getFactory().withoutOffer();
        }
        if (filterHasContribution == DisplayableFilterType.WITH) {
            getFactory().withContribution();
        } else if (filterHasContribution == DisplayableFilterType.WITHOUT) {
            getFactory().withoutContribution();
        }
    }

    @Override
    protected String getPageTitle() {
        return tr("Administration Kudosable");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected void doAddActions(final HtmlDropDown dropDown, final HtmlBranch block) {
        // Add actions into the drop down
        dropDown.addDropDownElements(new AdminActionManager().demandActions());

        // add a demand state selector
        final HtmlDropDown demandState = new HtmlDropDown(AdministrationAction.DEMAND_STATE_CODE);
        demandState.addDropDownElements(EnumSet.allOf(DisplayableDemandState.class));
        demandState.setLabel(tr("Change the demand state"));
        block.add(demandState);
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {
        
        addAsGroupFilter(form, url);
        addIsDeletedFilter(form, url);
        addPopularityStateFilter(form);
        
        final HtmlDropDown state = new HtmlDropDown(url.getFilterByStateParameter().formFieldData());
        state.addDropDownElements(EnumSet.allOf(DisplayableDemandState.class));
        state.setLabel(tr("Filter by demand state"));

        final HtmlDropDown hasSelectedOffer = new HtmlDropDown(url.getFilterSelectedOfferParameter().formFieldData());
        hasSelectedOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasSelectedOffer.setLabel(tr("Filter by selected offer"));

        final HtmlDropDown hasOffer = new HtmlDropDown(url.getFilterHasOfferParameter().formFieldData());
        hasOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasOffer.setLabel(tr("Filter by offer"));

        final HtmlDropDown hasContribution = new HtmlDropDown(url.getFilterHasContributionParameter().formFieldData());
        hasContribution.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasContribution.setLabel(tr("Filter by contribution"));

        form.add(state);
        form.add(hasSelectedOffer);
        form.add(hasOffer);
        form.add(hasContribution);
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<Demand> tableModel) {
        final DemandAdminPageUrl clonedUrl = url.clone();

        addAuthorColumn(tableModel);
        addCreationDateColumn(tableModel, clonedUrl);
        addPopularityColumn(tableModel, clonedUrl);
        addPopularityStateColumn(tableModel, clonedUrl);
        
        clonedUrl.setOrderByStr("demandState");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Demand state")), new StringColumnGenerator<Demand>() {
            @Override
            public String getStringBody(final Demand element) {
                return String.valueOf(element.getDemandState());
            }
        });

        clonedUrl.setOrderByStr("contribution");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("contribution")), new StringColumnGenerator<Demand>() {
            @Override
            public String getStringBody(final Demand element) {
                try {
                    return String.valueOf(element.getContribution());
                } catch (UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });

        tableModel.addColumn(tr("project"), new StringColumnGenerator<Demand>() {
            @Override
            public String getStringBody(final Demand element) {
                try {
                    return element.getProject().getName();
                } catch (UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });
    }

}
