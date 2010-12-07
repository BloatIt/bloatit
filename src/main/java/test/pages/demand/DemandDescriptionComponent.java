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
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlText;
import test.pages.demand.DemandPage.Request;

import com.bloatit.framework.Translation;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.server.Session;

public class DemandDescriptionComponent extends HtmlBlock {

    private HtmlText description;
    private HtmlText date;
    private HtmlText author;

    public DemandDescriptionComponent(Request request) {
        super();

        Session session = Context.getSession();
        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translatedDescription = request.demand.getDescription().getTranslationOrDefault(defaultLocale);
        description = new HtmlText(translatedDescription.getText());

        date = new HtmlText(HtmlTools.formatDate(session, request.demand.getCreationDate()), "description_date");
        author = new HtmlText(HtmlTools.generateLink(session, request.demand.getAuthor().getLogin(), new MemberPage(session, request.demand.getAuthor())),
                              "description_author");

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
        add(descriptionBlock);
    }
}
