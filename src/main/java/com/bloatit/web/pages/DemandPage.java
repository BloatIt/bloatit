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
package com.bloatit.web.pages;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Comment;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlKudoBox;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlProgressBar;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlString;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.PageNotFoundException;

public class DemandPage extends Page {

    private final Demand demand;

    public DemandPage(Session session, Map<String, String> parameters) {
        super(session, parameters);

        if (parameters.containsKey("id")) {
            Integer id = null;
            try {
                id = new Integer(parameters.get("id"));
                demand = DemandManager.GetDemandById(id);
            } catch (final NumberFormatException e) {
                throw new PageNotFoundException("Demand id not found " + id, null);
            }
        } else {
            demand = null;
        }

    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        if (demand == null) {
            throw new PageNotFoundException("Demand shouldn't be null", null);
        }
        this.demand = demand;
    }

    public DemandPage(Session session, Demand demand) {
        this(session, new HashMap<String, String>(), demand);
    }

    @Override
    protected HtmlComponent generateContent() {

        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);
        final HtmlContainer page = new HtmlContainer();

        final HtmlBlock left = new HtmlBlock("leftColumn");
        final HtmlBlock right = new HtmlBlock("rightColumn");
        page.add(new HtmlTitle(translatedDescription.getTitle(), "pageTitle"));
        page.add(left);
        page.add(right);

        // block avec la progression
        float progressValue = 0;
        // if(demand.getOffers().size() == 0) {
        progressValue = 100 * (1 - 1 / (1 + demand.getContribution().floatValue() / 200));
        // } else {
        // TODO
        // }

        HtmlForm contributeForm = new HtmlForm(new LogoutAction(session));
        HtmlButton contributeButton = new HtmlButton(session.tr("Contribuer"));

        contributeForm.add(contributeButton);

        final HtmlBlock contributeBlock = new HtmlBlock("contribute_block");
        contributeBlock.add(contributeForm);

        final HtmlBlock progressBlock = new HtmlBlock("progress_block");
        final HtmlProgressBar progressBar = new HtmlProgressBar(progressValue);

        final HtmlBlock progressBarBlock = new HtmlBlock("column");
        progressBarBlock.add(progressBar);

        progressBlock.add(contributeBlock);
        progressBlock.add(new HtmlText(demand.getContribution().toPlainString() + "€"));
        progressBlock.add(progressBarBlock);

        left.add(progressBlock);

        // Description
        generateDescription(left, translatedDescription);

        // Comments

        HtmlBlock commentsBlock = new HtmlBlock("comments_block");

        commentsBlock.add(new HtmlTitle(session.tr("Comments"), "comments_title"));

        for (Comment comment : demand.getComments()) {
            HtmlBlock commentBlock = new HtmlBlock("main_comment_block");
            commentBlock.add(new HtmlText(comment.getText()));

            generateChildComment(commentBlock, comment.getChildren());

            commentsBlock.add(commentBlock);

        }

        left.add(commentsBlock);

        // droite process

        HtmlBlock rightBlock = new HtmlBlock("right_block");

        HtmlBlock abstractBlock = new HtmlBlock("abstract_block");
        HtmlBlock timelineBlock = new HtmlBlock("timeline_block");
        HtmlBlock contributorsBlock = new HtmlBlock("contributors_block");

        rightBlock.add(abstractBlock);
        rightBlock.add(timelineBlock);
        rightBlock.add(contributorsBlock);

        generateAbstractBlock(abstractBlock);
        generateTimelineBlock(timelineBlock);
        generateContributorsBlock(contributorsBlock);

        right.add(rightBlock);

        return page;

    }

    @Override
    public String getCode() {
        if (demand != null) {
            return new HtmlString(session).add("demand/id-" + demand.getId() + "/title-").secure(demand.getTitle()).toString();
        } else {
            return "demand"; // TODO Faire un système pour afficher une page
                             // d'erreur
        }
    }

    @Override
    public String getTitle() {
        return "Demand ...";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private void generateContributorsBlock(HtmlBlock contributorsBlock) {

        int contributionCount = demand.getContributions().size();
        contributorsBlock.add(new HtmlText("" + contributionCount + session.tr("&nbsp;contributions")));

        if (contributionCount > 0) {
            float contributionMean = demand.getContribution().floatValue() / contributionCount;
            String contributionMin = demand.getContributionMin().toPlainString();
            String contributionMax = demand.getContributionMax().toPlainString();

            contributorsBlock.add(new HtmlText(session.tr("Min:&nbsp;") + contributionMin));
            contributorsBlock.add(new HtmlText(session.tr("Max:&nbsp;") + contributionMax));
            contributorsBlock.add(new HtmlText(session.tr("Mean:&nbsp;") + contributionMean));
        }

    }

    private void generateAbstractBlock(HtmlBlock abstractBlock) {
        HtmlForm contributeForm = new HtmlForm(new LogoutAction(session));
        HtmlButton contributeButton = new HtmlButton(session.tr("Make an offer"));

        contributeForm.add(contributeButton);
        abstractBlock.add(contributeForm);

    }

    private void generateTimelineBlock(HtmlBlock contributorsBlock) {
        HtmlList timelineList = new HtmlList();

        HtmlListItem creationDate = new HtmlListItem("Creation: " + demand.getCreationDate().toString());
        timelineList.addItem(creationDate);
        contributorsBlock.add(timelineList);
    }

    private void generateDescription(HtmlBlock left, Translation translatedDescription) {
        HtmlBlock descriptionBlock = new HtmlBlock("description_block");

        HtmlBlock descriptionKudoBlock = new HtmlBlock("description_kudo_block");
        HtmlKudoBox kudoBox = new HtmlKudoBox(demand, session);
        descriptionKudoBlock.add(kudoBox);

        HtmlText description = new HtmlText(translatedDescription.getText());
        descriptionBlock.add(descriptionKudoBlock);
        descriptionBlock.add(description);
        left.add(descriptionBlock);

    }

    private void generateChildComment(HtmlBlock commentBlock, PageIterable<Comment> children) {
        for (Comment childComment : children) {
            HtmlBlock childCommentBlock = new HtmlBlock("child_comment_block");
            childCommentBlock.add(new HtmlText(childComment.getText()));
            generateChildComment(childCommentBlock, childComment.getChildren());

            commentBlock.add(childCommentBlock);
        }
    }
}
