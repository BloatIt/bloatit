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
package com.bloatit.web.pages.demand;

import com.bloatit.framework.Offer;
import com.bloatit.framework.Translation;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlImage;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.pages.demand.DemandPage;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.components.PageComponent;
import com.bloatit.web.server.Session;
import java.util.Locale;

public class DemandDescriptionComponent extends PageComponent {

    private final DemandPage demandPage;
    private HtmlText description;
    private HtmlText date;
    private HtmlText author;

    public DemandDescriptionComponent(DemandPage demandPage) {
        super(demandPage.getSession());
        this.demandPage = demandPage;

    }

    @Override
    protected HtmlComponent produce() {
        // Description

        HtmlBlock descriptionBlock = new HtmlBlock("description_block");
        {

            HtmlBlock descriptionFooter = new HtmlBlock("description_footer");
            {

                HtmlBlock descriptionDetails = new HtmlBlock("description_details");
                {
                    descriptionDetails.add(author);
                    descriptionDetails.add(date);
                }

                descriptionBlock.add(descriptionDetails);

                descriptionBlock.add(description);
            }
            descriptionBlock.add(descriptionFooter);

        }
        return descriptionBlock;
    }

    @Override
    protected void extractData() {

        Locale defaultLocale = demandPage.getSession().getLanguage().getLocale();
        Translation translatedDescription = demandPage.getDemand().getDescription().getTranslationOrDefault(defaultLocale);
        description = new HtmlText(translatedDescription.getText());

        date = new HtmlText(HtmlTools.formatDate(session, demandPage.getDemand().getCreationDate()), "description_date");
        author = new HtmlText(HtmlTools.generateLink(session, demandPage.getDemand().getAuthor().getLogin(), new MemberPage(session, demandPage.getDemand().getAuthor())), "description_author");

    }
}
