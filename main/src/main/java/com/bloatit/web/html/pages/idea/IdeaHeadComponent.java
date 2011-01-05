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

import java.text.NumberFormat;
import java.util.Locale;

import com.bloatit.common.Image;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.html.components.custom.HtmlKudoBlock;
import com.bloatit.web.html.components.custom.HtmlProgressBar;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.CurrencyLocale;
import com.bloatit.web.utils.url.OfferPageUrl;

public class IdeaHeadComponent extends HtmlPageComponent {

	public IdeaHeadComponent(final Demand idea) {
		super();
		/*
		 * final HtmlDiv demandHead = new HtmlDiv("demand_head"); { // Add
		 * progress bar final HtmlDiv demandHeadProgress = new
		 * HtmlDiv("demand_head_progress"); { demandHeadProgress.add(new
		 * IdeaProgressBarComponent(demand)); }
		 * demandHead.add(demandHeadProgress);
		 * 
		 * // Add kudo box final HtmlDiv demandHeadKudo = new
		 * HtmlDiv("demand_head_kudo"); { demandHeadKudo.add(new
		 * IdeaKudoComponent(demand)); } demandHead.add(demandHeadKudo);
		 * 
		 * }
		 */

		final HtmlDiv ideaBlock = new HtmlDiv("idea_summary");
		{

			final HtmlDiv leftBlock = new HtmlDiv("idea_summary_left");
			{

				final HtmlDiv karmaBlock = new HtmlDiv("idea_karma");
				karmaBlock.add(new HtmlKudoBlock(idea, Context.getSession()));

				leftBlock.add(karmaBlock);

			}
			ideaBlock.add(leftBlock);

			final HtmlDiv centerBlock = new HtmlDiv("idea_summary_center");
			{

				final Locale defaultLocale = Context.getLocalizator().getLocale();
				final Translation translatedDescription = idea.getDescription().getTranslationOrDefault(defaultLocale);
				String shortDescription = translatedDescription.getText();

				if (shortDescription.length() > 144) {
					// TODO create a tools to truncate less dirty
					shortDescription = shortDescription.substring(0, 143) + " ...";
				}

				final HtmlParagraph text = new HtmlParagraph(shortDescription);
				text.setCssClass("idea_link_text");
				centerBlock.add(text);

				float progressValue = (float) Math.floor(idea.getProgression());
				float cappedProgressValue = progressValue;
				if (cappedProgressValue > 100) {
					cappedProgressValue = 100;
				}

				final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
				centerBlock.add(progressBar);

				if (idea.getCurrentOffer() == null) {

					HtmlGenericElement amount = new HtmlGenericElement("span");
					amount.setCssClass("important");

					CurrencyLocale currency = Context.getLocalizator().getCurrency(idea.getContribution());

					amount.addText(currency.getDefaultString());

					final HtmlParagraph progressText = new HtmlParagraph();
					progressText.setCssClass("idea_progress_text");

					progressText.add(amount);
					progressText.addText(Context.tr(" no offer ("));
					progressText.add(new OfferPageUrl(idea).getHtmlLink(Context.tr("make an offer")));
					progressText.addText(Context.tr(")"));

					centerBlock.add(progressText);
				} else {

					// Amount
					CurrencyLocale amountCurrency = Context.getLocalizator().getCurrency(idea.getContribution());
					HtmlGenericElement amount = new HtmlGenericElement("span");
					amount.setCssClass("important");
					amount.addText(amountCurrency.getDefaultString());

					// Target
					CurrencyLocale targetCurrency = Context.getLocalizator().getCurrency(idea.getCurrentOffer().getAmount());
					HtmlGenericElement target = new HtmlGenericElement("span");
					target.setCssClass("important");
					target.addText(targetCurrency.getDefaultString());

					// Progress
					HtmlGenericElement progress = new HtmlGenericElement("span");
					progress.setCssClass("important");
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
