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

import java.util.Locale;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.custom.renderer.HtmlRawTextRenderer;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.MemberPageUrl;

public class IdeaDescriptionComponent extends HtmlDiv {

    public IdeaDescriptionComponent(final Demand demand) {
        super();

        final Locale defaultLocale = Context.getLocalizator().getLocale();
        HtmlParagraph description = null;
        try {
            final Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
            description = new HtmlParagraph(new HtmlRawTextRenderer(translatedDescription.getText()));
        } catch (UnauthorizedOperationException e1) {
            // Nothing.
        }

        final HtmlParagraph date = new HtmlParagraph(HtmlTools.formatDate(Context.getLocalizator().getDate(demand.getCreationDate())),
                "description_date");
        final MemberPageUrl memberUrl = new MemberPageUrl(demand.getAuthor());

        final HtmlDiv descriptionBlock = new HtmlDiv("description_block");
        {
            final HtmlDiv descriptionFooter = new HtmlDiv("description_footer");
            {
                final HtmlDiv descriptionDetails = new HtmlDiv("description_details");
                {
                    try {
                        descriptionDetails.add(memberUrl.getHtmlLink(demand.getAuthor().getLogin()));
                    } catch (UnauthorizedOperationException e) {
                        // do nothing.
                    }
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
