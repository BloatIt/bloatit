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
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlText;

import test.Request;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.server.Session;
import test.UrlBuilder;
import test.html.HtmlTools;
import test.html.components.standard.HtmlLink;
import test.pages.MemberPage;

public class DemandDescriptionComponent extends HtmlDiv {

    private HtmlText description;
    private HtmlText date;
    private HtmlLink author;

    public DemandDescriptionComponent(Request request, Demand demand) {
        super();

        Session session = Context.getSession();
        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        description = new HtmlText(translatedDescription.getText());

        date = new HtmlText(HtmlTools.formatDate(session, demand.getCreationDate()), "description_date");

        UrlBuilder urlBuilder = new UrlBuilder(MemberPage.class);
        urlBuilder.addParameter("member", demand.getAuthor());

        author = urlBuilder.getHtmlLink(demand.getAuthor().getLogin());

        HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {

            HtmlDiv descriptionFooter = new HtmlDiv("description_footer");
            {

                HtmlDiv descriptionDetails = new HtmlDiv("description_details");
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
