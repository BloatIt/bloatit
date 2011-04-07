package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoFeature;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.model.Feature;
import com.bloatit.model.admin.FeatureAdminListFactory;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.url.FeatureAdminPageUrl;

@ParamContainer("admin/features")
public final class FeatureAdminPage extends KudosableAdminPage<DaoFeature, Feature, FeatureAdminListFactory> {

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFeatureState filterByState;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterSelectedOffer;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterHasOffer;

    @RequestParam(role = RequestParam.Role.POST)
    protected DisplayableFilterType filterHasContribution;

    private final FeatureAdminPageUrl url;

    public FeatureAdminPage(final FeatureAdminPageUrl url) {
        super(url, new FeatureAdminListFactory());
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
        if (filterByState != null && filterByState != DisplayableFeatureState.NO_FILTER) {
            getFactory().stateEquals(DisplayableFeatureState.getFeatureState(filterByState));
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
        dropDown.addDropDownElements(new AdminActionManager().featureActions());

        // add a feature state selector
        final HtmlDropDown featureState = new HtmlDropDown(AdministrationAction.FEATURE_STATE_CODE);
        featureState.addDropDownElements(EnumSet.allOf(DisplayableFeatureState.class));
        featureState.setLabel(tr("Change the feature state"));
        block.add(featureState);
    }

    @Override
    protected void addFormFilters(final HtmlForm form) {

        addAsTeamFilter(form, url);
        addIsDeletedFilter(form, url);
        addPopularityStateFilter(form);

        final FieldData stateData = url.getFilterByStateParameter().pickFieldData();
        final HtmlDropDown stateInput = new HtmlDropDown(stateData.getName());
        stateInput.setDefaultValue(stateData.getSuggestedValue());
        stateInput.addErrorMessages(stateData.getErrorMessages());
        stateInput.addDropDownElements(EnumSet.allOf(DisplayableFeatureState.class));
        stateInput.setLabel(tr("Filter by feature state"));

        final FieldData hasSelectedOfferData = url.getFilterSelectedOfferParameter().pickFieldData();
        final HtmlDropDown hasSelectedOffer = new HtmlDropDown(hasSelectedOfferData.getName());
        hasSelectedOffer.setDefaultValue(hasSelectedOfferData.getSuggestedValue());
        hasSelectedOffer.addErrorMessages(hasSelectedOfferData.getErrorMessages());
        hasSelectedOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasSelectedOffer.setLabel(tr("Filter by selected offer"));

        final FieldData hasOfferData = url.getFilterHasOfferParameter().pickFieldData();
        final HtmlDropDown hasOffer = new HtmlDropDown(hasOfferData.getName());
        hasOffer.setDefaultValue(hasOfferData.getSuggestedValue());
        hasOffer.addErrorMessages(hasOfferData.getErrorMessages());
        hasOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasOffer.setLabel(tr("Filter by offer"));

        final FieldData hasContributionData = url.getFilterHasContributionParameter().pickFieldData();
        final HtmlDropDown hasContribution = new HtmlDropDown(hasContributionData.getName());
        hasContribution.setDefaultValue(hasContributionData.getSuggestedValue());
        hasContribution.addErrorMessages(hasContributionData.getErrorMessages());
        hasContribution.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasContribution.setLabel(tr("Filter by contribution"));

        form.add(stateInput);
        form.add(hasSelectedOffer);
        form.add(hasOffer);
        form.add(hasContribution);
    }

    @Override
    protected void addColumns(final HtmlGenericTableModel<Feature> tableModel) {
        final FeatureAdminPageUrl clonedUrl = url.clone();

        addAuthorColumn(tableModel);
        addCreationDateColumn(tableModel, clonedUrl);
        addPopularityColumn(tableModel, clonedUrl);
        addPopularityStateColumn(tableModel, clonedUrl);

        clonedUrl.setOrderByStr("featureState");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Feature state")), new StringColumnGenerator<Feature>() {
            @Override
            public String getStringBody(final Feature element) {
                return String.valueOf(element.getFeatureState());
            }
        });

        clonedUrl.setOrderByStr("contribution");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("contribution")), new StringColumnGenerator<Feature>() {
            @Override
            public String getStringBody(final Feature element) {
                try {
                    return String.valueOf(element.getContribution());
                } catch (final UnauthorizedOperationException e) {
                    throw new ShallNotPassException("UnauthorizedOperationException on admin page", e);
                }
            }
        });

        tableModel.addColumn(tr("software"), new StringColumnGenerator<Feature>() {
            @Override
            public String getStringBody(final Feature element) {
                try {
                    return element.getSoftware().getName();
                } catch (final UnauthorizedOperationException e) {
                    throw new ShallNotPassException("UnauthorizedOperationException on admin page", e);
                }
            }
        });
    }

    @Override
    public void processErrors() throws RedirectException {
        session.notifyList(url.getMessages());
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return FeatureAdminPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new FeatureAdminPageUrl().getHtmlLink(tr("Feature administration")));

        return breadcrumb;
    }

}
