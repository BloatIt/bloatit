/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html.components.custom;

import static com.bloatit.web.server.Context.tr;
import static com.bloatit.web.server.Context.trn;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Offer;
import com.bloatit.framework.Translation;
import com.bloatit.framework.demand.Demand;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.CurrencyLocale;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.OfferPageUrl;

public final class HtmlIdeaSumary extends HtmlDiv {

    private static final int SHORT_TEXT_CONSTANT = 50;
    private static final int SHORT_DESCRIPTION_LENGTH = 220;
    private static final String IMPORTANT_CSS_CLASS = "important";

    public HtmlIdeaSumary(final Demand idea) {
        super("idea_conpact_summary");

        final HtmlDiv leftBlock = new HtmlDiv("idea_summary_left");
        {

            final HtmlDiv karmaBlock = new HtmlDiv("idea_karma");
            karmaBlock.add(new HtmlParagraph("" + idea.getPopularity()));

            leftBlock.add(karmaBlock);

        }
        add(leftBlock);

        final HtmlDiv centerBlock = new HtmlDiv("idea_summary_center");
        {

            final HtmlSpan project = new HtmlSpan();
            project.setCssClass("project");
            project.addText("VLC");

            final HtmlLink linkTitle = new IdeaPageUrl(idea).getHtmlLink("");
            linkTitle.setCssClass("idea_link");

            linkTitle.add(project);
            linkTitle.addText(" - ");
            try {
                linkTitle.addText(idea.getTitle());
            } catch (final UnauthorizedOperationException e1) {
                linkTitle.addText(tr("Error: you do not have the right to see te title."));
            }

            final HtmlTitleBlock ideaTitle = new HtmlTitleBlock(linkTitle, 3);
            {

                final Locale defaultLocale = Context.getLocalizator().getLocale();
                String shortDescription = tr("Error: you do not have the right to see the description.");
                try {
                    final Translation translatedDescription = idea.getDescription().getTranslationOrDefault(defaultLocale);
                    shortDescription = translatedDescription.getShortText(SHORT_DESCRIPTION_LENGTH, SHORT_TEXT_CONSTANT);
                } catch (final UnauthorizedOperationException e1) {
                    // Do nothing.
                }

                final HtmlLink linkText = new IdeaPageUrl(idea).getHtmlLink(new HtmlParagraph(shortDescription));
                linkText.setCssClass("idea_link_text");

                ideaTitle.add(linkText);

                float progressValue = 0;
                try {
                    progressValue = (float) Math.floor(idea.getProgression());
                    float cappedProgressValue = progressValue;
                    if (cappedProgressValue > Demand.PROGRESSION_PERCENT) {
                        cappedProgressValue = Demand.PROGRESSION_PERCENT;
                    }

                    final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                    ideaTitle.add(progressBar);
                } catch (final UnauthorizedOperationException e) {
                    // The user doesn't have the right to see the progress, a text replace
                    // the progress bar.
                    final HtmlParagraph progressBarText = new HtmlParagraph(tr("You don't have the right to see the progress on the idea."));
                    ideaTitle.add(progressBarText);
                }

                Offer currentOffer = null;
                try {
                    currentOffer = idea.getSelectedOffer();
                } catch (final UnauthorizedOperationException e1) {
                    // Do nothing.
                }
                if (currentOffer == null) {

                    final HtmlSpan amount = new HtmlSpan();
                    amount.setCssClass(IMPORTANT_CSS_CLASS);

                    CurrencyLocale currency;
                    try {
                        currency = Context.getLocalizator().getCurrency(idea.getContribution());

                        amount.addText(currency.getDefaultString());
                    } catch (final UnauthorizedOperationException e) {
                        // The user doesn't have the right to see the contribution,
                        // nothing is displayed
                    }

                    final HtmlParagraph progressText = new HtmlParagraph();
                    progressText.setCssClass("idea_progress_text");

                    progressText.add(amount);
                    progressText.addText(tr(" no offer ("));
                    progressText.add(new OfferPageUrl(idea).getHtmlLink(tr("make an offer")));
                    progressText.addText(tr(")"));

                    ideaTitle.add(progressText);
                } else {
                    // Amount
                    CurrencyLocale amountCurrency;
                    try {
                        amountCurrency = Context.getLocalizator().getCurrency(idea.getContribution());
                        final HtmlSpan amount = new HtmlSpan();
                        amount.setCssClass(IMPORTANT_CSS_CLASS);
                        amount.addText(amountCurrency.getDefaultString());

                        // Target
                        final CurrencyLocale targetCurrency = Context.getLocalizator().getCurrency(currentOffer.getAmount());
                        final HtmlSpan target = new HtmlSpan();
                        target.setCssClass(IMPORTANT_CSS_CLASS);
                        target.addText(targetCurrency.getDefaultString());

                        // Progress
                        final HtmlSpan progress = new HtmlSpan();
                        progress.setCssClass(IMPORTANT_CSS_CLASS);
                        final NumberFormat format = NumberFormat.getNumberInstance();
                        format.setMinimumFractionDigits(0);
                        progress.addText("" + format.format(progressValue) + " %");

                        final HtmlParagraph progressText = new HtmlParagraph();
                        progressText.setCssClass("idea_progress_text");

                        progressText.add(amount);
                        progressText.addText(tr(" i.e. "));
                        progressText.add(progress);
                        progressText.addText(tr(" of "));
                        progressText.add(target);
                        final long amountLong = currentOffer.getAmount().longValue();
                        progressText.addText(trn(" requested ", " requested ", amountLong));

                        ideaTitle.add(progressText);
                    } catch (final UnauthorizedOperationException e) {
                        // The user doesn't have the right to see the progress, a text
                        // replace the progress bar.
                        final HtmlParagraph progressBarText = new HtmlParagraph(tr("You don't have the right to see the progress on the idea."));
                        ideaTitle.add(progressBarText);
                    }
                }

            }
            centerBlock.add(ideaTitle);

        }
        // ideaLinkBlock.add(centerBlock);
        add(centerBlock);

        final HtmlDiv rightBlock = new HtmlDiv("idea_summary_right");
        {
            rightBlock.add(new HtmlImage(new Image("/resources/img/idea.png", Image.ImageType.DISTANT)));
        }
        // ideaLinkBlock.add(rightBlock);
        add(rightBlock);
    }

}
