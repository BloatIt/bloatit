/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.pages.demand;

import java.util.Locale;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.pages.MemberPage;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.UrlBuilder;

public class DemandDescriptionComponent extends HtmlDiv {

    private final HtmlParagraph description;
    private final HtmlParagraph date;
    private final HtmlLink author;

    public DemandDescriptionComponent(final Demand demand) {
        super();

        final Session session = Context.getSession();
        final Locale defaultLocale = session.getLanguage().getLocale();
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        description = new HtmlParagraph(translatedDescription.getText());

        date = new HtmlParagraph(HtmlTools.formatDate(session, demand.getCreationDate()), "description_date");

        final UrlBuilder urlBuilder = new UrlBuilder(MemberPage.class);
        urlBuilder.addParameter(MemberPage.MEMBER_FIELD_NAME, demand.getAuthor());

        author = urlBuilder.getHtmlLink(demand.getAuthor().getLogin());

        final HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {

            final HtmlDiv descriptionFooter = new HtmlDiv("description_footer");
            {

                final HtmlDiv descriptionDetails = new HtmlDiv("description_details");
                {
                    descriptionDetails.add(author);
                    descriptionDetails.add(date);
                }

                descriptionBlock.add(descriptionDetails);

                descriptionBlock.add(description);
            }
            descriptionBlock.add(descriptionFooter);

        }
        add(descriptionBlock);
    }
}
