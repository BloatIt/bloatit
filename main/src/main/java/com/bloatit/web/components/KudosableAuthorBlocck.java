package com.bloatit.web.components;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.model.KudosableInterface;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.url.PopularityVoteActionUrl;

public class KudosableAuthorBlocck extends UserContentAuthorBlock {

    public KudosableAuthorBlocck(final KudosableInterface<?> kudosable) {
        super(kudosable);

        add(new HtmlText(" – "));

        // ////////////////////
        // Popularity
        final HtmlSpan commentPopularity = new HtmlSpan("comment_populatity");
        {

            commentPopularity.addText(tr("Popularity: {0}", HtmlTools.compressKarma(kudosable.getPopularity())));

            if (!kudosable.isOwner()) {
                final int vote = kudosable.getUserVoteValue();
                if (vote == 0) {
                    commentPopularity.addText(" (");

                    // Useful
                    final PopularityVoteActionUrl usefullUrl = new PopularityVoteActionUrl(kudosable, true);
                    final HtmlLink usefullLink = usefullUrl.getHtmlLink(tr("Useful"));
                    usefullLink.setCssClass("useful");

                    // Useless
                    final PopularityVoteActionUrl uselessUrl = new PopularityVoteActionUrl(kudosable, false);
                    final HtmlLink uselessLink = uselessUrl.getHtmlLink(tr("Useless"));
                    uselessLink.setCssClass("useless");

                    commentPopularity.add(usefullLink);
                    commentPopularity.addText(" – ");
                    commentPopularity.add(uselessLink);

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
