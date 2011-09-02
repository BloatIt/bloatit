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
package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.KudosableInterface;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.PopularityVoteActionUrl;

public class KudosableAuthorBlock extends UserContentAuthorBlock {

    public KudosableAuthorBlock(final KudosableInterface kudosable) {
        super(kudosable);

        add(new HtmlText(" – "));

        // ////////////////////
        // Popularity
        final HtmlSpan commentPopularity = new HtmlSpan("kudosable_popularity");
        {

            commentPopularity.addText(tr("Popularity: {0}", HtmlTools.compressKarma(kudosable.getPopularity())));

            if (!kudosable.getRights().isOwner()) {
                final int vote = kudosable.getUserVoteValue();
                if (vote == 0) {
                    commentPopularity.addText(" (");

                    if (!kudosable.canVoteUp().isEmpty()) {
                        final PopularityVoteActionUrl usefullUrl = new PopularityVoteActionUrl(Context.getSession().getShortKey(), kudosable, true);
                        final HtmlLink usefullLink = usefullUrl.getHtmlLink(tr("Useful"));
                        usefullLink.setCssClass("useful");
                        commentPopularity.add(usefullLink);
                        commentPopularity.addText(" – ");
                    }

                    if (!kudosable.canVoteDown().isEmpty()) {
                        final PopularityVoteActionUrl uselessUrl = new PopularityVoteActionUrl(Context.getSession().getShortKey(), kudosable, false);
                        final HtmlLink uselessLink = uselessUrl.getHtmlLink(tr("Useless"));
                        uselessLink.setCssClass("useless");
                        commentPopularity.add(uselessLink);
                    }

                    commentPopularity.addText(")");
                } else {
                    // Already voted
                    final HtmlSpan voted = new HtmlSpan("comment_voted");
                    {
                        if (vote > 0) {
                            voted.addText("+" + vote);
                            voted.setCssClass("comment_voted Useful");
                        } else {
                            voted.addText("−" + Math.abs(vote));
                            voted.setCssClass("comment_voted useless");
                        }
                    }
                    commentPopularity.add(voted);
                }
            }
        }
        add(commentPopularity);
    }

}
