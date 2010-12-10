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
package test.pages.demand;

import java.util.Locale;

import test.Context;
import test.Request;
import test.UrlBuilder;
import test.html.HtmlTools;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlLink;
import test.html.components.standard.HtmlText;
import test.pages.MemberPage;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.server.Session;

public class DemandDescriptionComponent extends HtmlDiv {

    private final HtmlText description;
    private final HtmlText date;
    private final HtmlLink author;

    public DemandDescriptionComponent(final Request request, final Demand demand) {
        super();

        final Session session = Context.getSession();
        final Locale defaultLocale = session.getLanguage().getLocale();
        final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        description = new HtmlText(translatedDescription.getText());

        date = new HtmlText(HtmlTools.formatDate(session, demand.getCreationDate()), "description_date");

        final UrlBuilder urlBuilder = new UrlBuilder(MemberPage.class);
        urlBuilder.addParameter("member", demand.getAuthor());

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
