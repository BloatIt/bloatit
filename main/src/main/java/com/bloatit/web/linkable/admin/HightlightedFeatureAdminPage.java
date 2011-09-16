/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDateField;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Member;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.HighlightFeatureManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.linkable.features.FeatureTabPane.FeatureTabKey;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.pages.tools.HightlightedFeaturesTools;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.DeclareHightlightedFeatureActionUrl;
import com.bloatit.web.url.FeaturePageUrl;
import com.bloatit.web.url.HightlightedFeatureAdminPageUrl;

@ParamContainer("admin/highlightedfeatures")
public class HightlightedFeatureAdminPage extends AdminPage {
    private final HightlightedFeatureAdminPageUrl url;

    /**
     * @param url
     */
    public HightlightedFeatureAdminPage(final HightlightedFeatureAdminPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateConfAdmin());
        return layout;
    }

    private HtmlElement generateConfAdmin() {
        final HtmlTitleBlock master = new HtmlTitleBlock("Administrate highlighted features", 1);

        final PageIterable<HighlightFeature> hightlightedFeatures = HighlightFeatureManager.getAll();
        final List<HighlightFeature> activeHightlightedFeatures = HighlightFeatureManager.getPositionArray(6);

        final HtmlLineTableModel model = new HtmlLineTableModel();

        for (final HighlightFeature feature : hightlightedFeatures) {
            model.addLine(new FeatureLine(feature, activeHightlightedFeatures));
        }

        master.add(new HtmlTable(model));

        final DeclareHightlightedFeatureActionUrl actionUrl = new DeclareHightlightedFeatureActionUrl(getSession().getShortKey());

        final HtmlForm newHightlightedFeatureForm = new HtmlForm(actionUrl.urlString(), Method.POST);

        final HtmlTitle newHightlightedFeatureTitle = new HtmlTitle(2);
        newHightlightedFeatureTitle.addText("Define new highlighted feature");
        newHightlightedFeatureForm.add(newHightlightedFeatureTitle);

        // Position
        final FieldData positionFieldData = actionUrl.getPositionParameter().pickFieldData();
        final HtmlDropDown positionInput = new HtmlDropDown(positionFieldData.getName(), "Position");
        for (int i = 1; i <= 6; i++) {
            positionInput.addDropDownElement(String.valueOf(i), String.valueOf(i));
        }
        positionInput.setDefaultValue(positionFieldData.getSuggestedValue());
        newHightlightedFeatureForm.add(positionInput);

        // Feature
        final FieldData featureFieldData = actionUrl.getFeatureParameter().pickFieldData();
        final HtmlDropDown featureInput = new HtmlDropDown(featureFieldData.getName(), "Feature");
        final PageIterable<Feature> features = FeatureManager.getFeatures();
        for (final Feature feature : features) {
            featureInput.addDropDownElement(String.valueOf(feature.getId()), feature.getDescription().getTranslationOrDefault(Language.fromLocale(Context.getLocalizator().getLocale())).getTitle());
        }
        featureInput.setDefaultValue(featureFieldData.getSuggestedValue());
        newHightlightedFeatureForm.add(featureInput);

        // Reason
        final FieldData reasonFieldData = actionUrl.getTitleParameter().pickFieldData();
        final HtmlDropDown reasonInput = new HtmlDropDown(reasonFieldData.getName(), "Reason");
        final Map<String, String> reasonsMap = HightlightedFeaturesTools.getReasonsMap();
        for (final Entry<String, String> reason : reasonsMap.entrySet()) {
            reasonInput.addDropDownElement(reason.getKey(), reason.getValue());
        }
        reasonInput.setDefaultValue(reasonFieldData.getSuggestedValue());
        newHightlightedFeatureForm.add(reasonInput);

        // Dates
        final FieldData activationDateFieldData = actionUrl.getActivationDateParameter().pickFieldData();
        final HtmlDateField activationDateInput = new HtmlDateField(activationDateFieldData.getName(), Context.getLocalizator().getLocale());
        activationDateInput.setLabel("Activation date");
        activationDateInput.setDefaultValue(activationDateFieldData.getSuggestedValue());
        newHightlightedFeatureForm.add(activationDateInput);

        final FieldData deactivationDateFieldData = actionUrl.getDesactivationDateParameter().pickFieldData();
        final HtmlDateField deactivationDateInput = new HtmlDateField(deactivationDateFieldData.getName(), Context.getLocalizator().getLocale());
        deactivationDateInput.setLabel("Desactivation date");
        deactivationDateInput.setDefaultValue(deactivationDateFieldData.getSuggestedValue());
        newHightlightedFeatureForm.add(deactivationDateInput);

        // Submit button
        newHightlightedFeatureForm.add(new HtmlSubmit(tr("submit")));

        master.add(newHightlightedFeatureForm);

        return master;
    }

    @Override
    protected String createPageTitle() {
        return "Administrate highlighted features";
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.pushLink(new AdminHomePageUrl().getHtmlLink("Admin"));
        breadcrumb.pushLink(url.getHtmlLink("Highlighted features"));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private class FeatureLine extends HtmlTableLine {

        private FeatureLine(final HighlightFeature feature, final List<HighlightFeature> activeHightlightedFeatures) {
            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new HtmlText(String.valueOf(feature.getId()));
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new HtmlText(String.valueOf(feature.getPosition()));
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new HtmlText(HightlightedFeaturesTools.getReason(feature));
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new HtmlText(Context.getLocalizator().getDate(feature.getActivationDate()).toString(FormatStyle.FULL));
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new HtmlText(Context.getLocalizator().getDate(feature.getDesactivationDate()).toString(FormatStyle.FULL));
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    return new FeaturePageUrl(feature.getFeature(), FeatureTabKey.description).getHtmlLink(feature.getFeature().getDescription().getTranslationOrDefault(Language.fromLocale(Context.getLocalizator().getLocale())).getTitle());
                }
            });

            addCell(new HtmlTableCell("") {

                @Override
                public HtmlNode getBody() {
                    String status = "";
                    if (activeHightlightedFeatures.contains(feature)) {
                        status = "active";
                    } else if (feature.isActive()) {
                        status = "overwrited";
                    } else if (feature.getDesactivationDate().before(DateUtils.now())) {
                        status = "perimed";
                    } else if (feature.getActivationDate().after(DateUtils.now())) {
                        status = "future";
                    } else {
                        status = "There is a problem";
                        Log.web().error("Strange statue for hightlighted feature " + feature.getId());
                    }
                    return new HtmlText(status);
                }
            });

        }

    }

}
