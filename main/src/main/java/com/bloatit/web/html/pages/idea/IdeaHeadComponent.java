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
package com.bloatit.web.html.pages.idea;

import static com.bloatit.web.server.Context.tr;

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Offer;
import com.bloatit.framework.Translation;
import com.bloatit.web.html.components.custom.HtmlKudoBlock;
import com.bloatit.web.html.components.custom.HtmlProgressBar;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlSpan;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.CurrencyLocale;
import com.bloatit.web.utils.url.OfferPageUrl;

public final class IdeaHeadComponent extends HtmlPageComponent {

    private static final String IMPORTANT_CSS_CLASS = "important";
    private static final int SHORT_DESCRIPTION_LENGTH = 144;

    public IdeaHeadComponent(final Demand idea) {
        super();

        final HtmlDiv ideaBlock = new HtmlDiv("idea_summary");
        {

            final HtmlDiv leftBlock = new HtmlDiv("idea_summary_left");
            {

                final HtmlDiv karmaBlock = new HtmlDiv("idea_karma");
                karmaBlock.add(new HtmlKudoBlock(idea));

                leftBlock.add(karmaBlock);

            }
            ideaBlock.add(leftBlock);

            final HtmlDiv centerBlock = new HtmlDiv("idea_summary_center");
            {

                final Locale defaultLocale = Context.getLocalizator().getLocale();
                String shortDescription = tr("Error: you do not have the right to see the description.");
                try {
                    Translation translatedDescription = idea.getDescription().getTranslationOrDefault(defaultLocale);
                    shortDescription = translatedDescription.getText();
                } catch (UnauthorizedOperationException e1) {
                    // Do nothing.
                }
                if (shortDescription.length() > SHORT_DESCRIPTION_LENGTH) {
                    // TODO create a tools to truncate less dirty
                    shortDescription = shortDescription.substring(0, SHORT_DESCRIPTION_LENGTH - 1) + " ...";
                }

                final HtmlParagraph text = new HtmlParagraph(shortDescription);
                text.setCssClass("idea_link_text");
                centerBlock.add(text);

                float progressValue = 0;
                try {
                    progressValue = (float) Math.floor(idea.getProgression());
                    float cappedProgressValue = progressValue;
                    if (cappedProgressValue > Demand.PROGRESSION_PERCENT) {
                        cappedProgressValue = Demand.PROGRESSION_PERCENT;
                    }

                    final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                    centerBlock.add(progressBar);
                } catch (UnauthorizedOperationException e) {
                    // No right, no progress bar
                }

                Offer currentOffer = null;
                try {
                    currentOffer = idea.getCurrentOffer();
                } catch (UnauthorizedOperationException e1) {
                    // Nothing.
                }
                if (currentOffer == null) {

                    HtmlSpan amount = new HtmlSpan();
                    amount.setCssClass(IMPORTANT_CSS_CLASS);

                    CurrencyLocale currency;
                    try {
                        currency = Context.getLocalizator().getCurrency(idea.getContribution());

                        amount.addText(currency.getDefaultString());

                    } catch (UnauthorizedOperationException e) {
                        // No right, no display
                    }
                    final HtmlParagraph progressText = new HtmlParagraph();
                    progressText.setCssClass("idea_progress_text");

                    progressText.add(amount);
                    progressText.addText(Context.tr(" no offer ("));
                    progressText.add(new OfferPageUrl(idea).getHtmlLink(Context.tr("make an offer")));
                    progressText.addText(Context.tr(")"));

                    centerBlock.add(progressText);
                } else {

                    // Amount
                    CurrencyLocale amountCurrency;
                    try {
                        amountCurrency = Context.getLocalizator().getCurrency(idea.getContribution());
                        HtmlSpan amount = new HtmlSpan();
                        amount.setCssClass(IMPORTANT_CSS_CLASS);
                        amount.addText(amountCurrency.getDefaultString());

                        // Target
                        CurrencyLocale targetCurrency = Context.getLocalizator().getCurrency(currentOffer.getAmount());
                        HtmlSpan target = new HtmlSpan();
                        target.setCssClass(IMPORTANT_CSS_CLASS);
                        target.addText(targetCurrency.getDefaultString());

                        // Progress
                        HtmlSpan progress = new HtmlSpan();
                        progress.setCssClass(IMPORTANT_CSS_CLASS);
                        NumberFormat format = NumberFormat.getNumberInstance();
                        format.setMinimumFractionDigits(0);
                        progress.addText("" + format.format(progressValue) + " %");

                        final HtmlParagraph progressText = new HtmlParagraph();
                        progressText.setCssClass("idea_progress_text");

                        progressText.add(amount);
                        progressText.addText(Context.tr(" i.e. "));
                        progressText.add(progress);
                        progressText.addText(Context.tr(" of "));
                        progressText.add(target);
                        progressText.addText(Context.tr(" requested "));

                        centerBlock.add(progressText);
                    } catch (UnauthorizedOperationException e) {
                        // No right, no display
                    }
                }

            }
            ideaBlock.add(centerBlock);

            final HtmlDiv actionBlock = new HtmlDiv("idea_summary_action");
            {

                actionBlock.add(new IdeaContributeButtonComponent(idea));
                actionBlock.add(new IdeaMakeOfferButtonComponent(idea));

            }
            ideaBlock.add(actionBlock);

            final HtmlDiv rightBlock = new HtmlDiv("idea_summary_right");
            {
                rightBlock.add(new HtmlImage(new Image("/resources/img/idea.png", Image.ImageType.DISTANT)));
            }
            ideaBlock.add(rightBlock);
        }

        add(ideaBlock);
    }
}
