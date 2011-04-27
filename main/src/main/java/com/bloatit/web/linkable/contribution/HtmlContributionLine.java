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
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;

public class HtmlContributionLine extends HtmlDiv {

    protected HtmlContributionLine(final Feature feature, final BigDecimal amount, final Url editUrl) {
        super("quotation_detail_line");
        final Localizator localizator = Context.getLocalizator();

        add(SoftwaresTools.getSoftwareLogoSmall(feature.getSoftware()));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(feature.getContribution()).getSimpleEuroString()));
        add(new HtmlDiv("quotation_detail_line_money_image").add(new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up")));
        add(new HtmlDiv("quotation_detail_line_money").addText(localizator.getCurrency(feature.getContribution().add(amount)).getSimpleEuroString()));
        add(new HtmlDiv("quotation_detail_line_categorie").addText(tr("Contribution")));
        add(new HtmlDiv("quotation_detail_line_description").addText(FeaturesTools.getTitle(feature)));

        final HtmlDiv amountBlock = new HtmlDiv("quotation_detail_line_amount");
        amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(localizator.getCurrency(amount).getTwoDecimalEuroString()));
        
        // Modify contribution button
        if (editUrl != null) {
            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_modify").add(editUrl.getHtmlLink(tr("edit"))));
        }
        add(amountBlock);

    }
}
