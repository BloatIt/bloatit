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

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableCell;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Feature;
import com.bloatit.model.Image;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.features.FeaturesTools;
import com.bloatit.web.linkable.softwares.SoftwaresTools;

public class HtmlContributionLine extends HtmlTableLine {

    private final Feature feature;
    private final Localizator localizator;
    private final BigDecimal amount;
    private final Url editUrl;

    protected HtmlContributionLine(final Feature feature, final BigDecimal amount, final Url editUrl) {
        this.feature = feature;
        this.amount = amount;
        this.editUrl = editUrl;
        setCssClass("quotation_detail_line");

        localizator = Context.getLocalizator();

        addCell(new SoftwareLogoCell());
        addCell(new BeforeMoneyCell());
        addCell(new MoneyImageCell());
        addCell(new AfterMoneyCell());

        addCell(new CategorieCell());
        addCell(new DescriptionCell());

        addCell(new AmountCell());

    }

    private class SoftwareLogoCell extends HtmlTableCell {

        public SoftwareLogoCell() {
            super("");
        }

        @Override
        public HtmlNode getBody() {
            return new SoftwaresTools.SmallLogo(feature.getSoftware());
        }
    }

    private class BeforeMoneyCell extends HtmlTableCell {

        public BeforeMoneyCell() {
            super("quotation_detail_line_money");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlText(localizator.getCurrency(feature.getContribution()).getSimpleEuroString());
        }
    }

    private class AfterMoneyCell extends HtmlTableCell {

        public AfterMoneyCell() {
            super("quotation_detail_line_money");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlText(localizator.getCurrency(feature.getContribution().add(amount)).getSimpleEuroString());
        }
    }

    private class MoneyImageCell extends HtmlTableCell {

        public MoneyImageCell() {
            super("quotation_detail_line_money_image");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlImage(new Image(WebConfiguration.getImgMoneyUpSmall()), "money up");
        }
    }

    private class CategorieCell extends HtmlTableCell {

        public CategorieCell() {
            super("quotation_detail_line_categorie");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlDiv("").addText(tr("Contribution"));
        }
    }

    private class DescriptionCell extends HtmlTableCell {

        public DescriptionCell() {
            super("quotation_detail_line_description");
        }

        @Override
        public HtmlNode getBody() {
            return new HtmlDiv("").addText(FeaturesTools.getTitle(feature));
        }
    }

    private class AmountCell extends HtmlTableCell {

        public AmountCell() {
            super("quotation_detail_line_amount");
        }

        @Override
        public HtmlNode getBody() {

            final HtmlDiv amountBlock = new HtmlDiv();
            amountBlock.add(new HtmlDiv("quotation_detail_line_amount_money").addText(localizator.getCurrency(amount).getTwoDecimalEuroString()));

            // Modify contribution button
            if (editUrl != null) {
                amountBlock.add(new HtmlDiv("quotation_detail_line_amount_modify").add(editUrl.getHtmlLink(tr("edit"))));
            }
            return amountBlock;
        }
    }
}
