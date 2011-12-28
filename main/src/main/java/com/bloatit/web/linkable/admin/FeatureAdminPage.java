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

import com.bloatit.data.DaoFeature;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.admin.FeatureAdminListFactory;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.url.FeatureAdminPageUrl;

@ParamContainer("admin/features")
public final class FeatureAdminPage extends KudosableAdminPage<DaoFeature, Feature, FeatureAdminListFactory> {

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFeatureState filterByState;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterSelectedOffer;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterHasOffer;

    @RequestParam(role = RequestParam.Role.POST)
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterHasContribution;

    private final FeatureAdminPageUrl url;

    public FeatureAdminPage(final FeatureAdminPageUrl url) {
        super(url, new FeatureAdminListFactory());
        this.url = url;
        filterByState = url.getFilterByState();
        filterSelectedOffer = url.getFilterSelectedOffer();
        filterHasOffer = url.getFilterHasOffer();
        filterHasContribution = url.getFilterHasContribution();

        getSession().addParameter(url.getFilterByStateParameter());
        getSession().addParameter(url.getFilterSelectedOfferParameter());
        getSession().addParameter(url.getFilterHasOfferParameter());
        getSession().addParameter(url.getFilterHasContributionParameter());

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
    protected String createPageTitle() {
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
    protected void addFormFilters(final HtmlBranch form) {

        addAsTeamFilter(form, url);
        addIsDeletedFilter(form, url);
        addPopularityStateFilter(form);

        final FieldData stateData = url.getFilterByStateParameter().pickFieldData();
        final HtmlDropDown stateInput = new HtmlDropDown(stateData.getName());
        stateInput.addErrorMessages(stateData.getErrorMessages());
        stateInput.addDropDownElements(EnumSet.allOf(DisplayableFeatureState.class));
        stateInput.setLabel(tr("Filter by feature state"));

        final FieldData hasSelectedOfferData = url.getFilterSelectedOfferParameter().pickFieldData();
        final HtmlDropDown hasSelectedOffer = new HtmlDropDown(hasSelectedOfferData.getName());
        hasSelectedOffer.addErrorMessages(hasSelectedOfferData.getErrorMessages());
        hasSelectedOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasSelectedOffer.setLabel(tr("Filter by selected offer"));

        final FieldData hasOfferData = url.getFilterHasOfferParameter().pickFieldData();
        final HtmlDropDown hasOffer = new HtmlDropDown(hasOfferData.getName());
        hasOffer.addErrorMessages(hasOfferData.getErrorMessages());
        hasOffer.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        hasOffer.setLabel(tr("Filter by offer"));

        final FieldData hasContributionData = url.getFilterHasContributionParameter().pickFieldData();
        final HtmlDropDown hasContribution = new HtmlDropDown(hasContributionData.getName());
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
                return String.valueOf(element.getContribution());
            }
        });

        tableModel.addColumn(tr("software"), new StringColumnGenerator<Feature>() {
            @Override
            public String getStringBody(final Feature element) {
                return element.getSoftware() == null ? "No software" : element.getSoftware().getName();
            }
        });
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return FeatureAdminPage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new FeatureAdminPageUrl().getHtmlLink(tr("Feature administration")));

        return breadcrumb;
    }

}
