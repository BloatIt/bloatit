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

import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlRenderer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Comment;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlKudoBox;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPagedList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlProgressBar;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlString;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTabBlock;
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
                demand = DemandManager.getDemandById(id);
            } catch (final NumberFormatException e) {
                throw new PageNotFoundException("Demand id not found " + id, null);
            }
        } else {
            demand = null;
        }
        generateOutputParams();


    }

    public DemandPage(Session session, Map<String, String> parameters, Demand demand) {
        super(session, parameters);
        if (demand == null) {
            throw new PageNotFoundException("Demand shouldn't be null", null);
        }
        this.demand = demand;
        generateOutputParams();
    }

    public DemandPage(Session session, Demand demand) {
        this(session, new HashMap<String, String>(), demand);
    }

    @Override
    protected HtmlComponent generateContent() {
        
        needCustomDesign();

        final HtmlContainer page = new HtmlContainer();

        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);

        page.add(new HtmlTitle(translatedDescription.getTitle(), "pageTitle"));

        page.add(generateHead());
        page.add(generateBody());

        

        return page;

    }

    @Override
    public String getCode() {
        return "demand";
    }

    @Override
    public String getTitle() {
        return "Demand ...";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private HtmlBlock generateContributorsBlock() {

        HtmlBlock contributorsBlock = new HtmlBlock("contributors_block");


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


        HtmlRenderer<Contribution> participationRenderer = new HtmlRenderer<Contribution>() {

            @Override
            public void generate(HtmlResult htmlResult, Contribution item) {
               
                String itemString =  item.getAuthor().getLogin() + " "+ item.getAmount().toPlainString() + " " + item.getCreationDate().toString();

                HtmlListItem htmlItem = new HtmlListItem(itemString);

                htmlItem.generate(htmlResult);
            }
        };



        HtmlPagedList<Contribution> participationsList = new HtmlPagedList<Contribution>("participation_list", participationRenderer, demand.getContributions(), this, session);


        contributorsBlock.add(participationsList);

        return contributorsBlock;

    }

    private HtmlBlock generateTimelineBlock() {
        HtmlBlock timelineBlock = new HtmlBlock("timeline_block");

        HtmlList timelineList = new HtmlList();

        HtmlListItem creationDate = new HtmlListItem("Creation: " + demand.getCreationDate().toString());
        timelineList.addItem(creationDate);
        timelineBlock.add(timelineList);

        return timelineBlock;
    }

    private HtmlBlock generateDescription() {

        // Description
        Locale defaultLocale = session.getLanguage().getLocale();
        Translation translatedDescription = demand.getDescription().getTranslationOrDefault(defaultLocale);


        HtmlBlock descriptionBlock = new HtmlBlock("description_block");

        
        
        

        HtmlText description = new HtmlText(translatedDescription.getText());
        
        HtmlBlock descriptionFooter = new HtmlBlock("description_footer");


        descriptionBlock.add(generateDescriptionDetails());

        descriptionBlock.add(description);
        descriptionBlock.add(descriptionFooter);
        return descriptionBlock;

    }

    private void generateChildComment(HtmlBlock commentBlock, PageIterable<Comment> children) {
        for (Comment childComment : children) {
            HtmlBlock childCommentBlock = new HtmlBlock("child_comment_block");
            String date = "<span class=\"comment_date\">"+HtmlTools.formatDate(session,childComment.getCreationDate())+"</span>";
            String author = "<span class=\"comment_author\">"+HtmlTools.generateLink(session, childComment.getAuthor().getLogin() , new MemberPage(session, childComment.getAuthor()))+"</span>";
            childCommentBlock.add(new HtmlText(childComment.getText()+" – "+author+" "+date));
            generateChildComment(childCommentBlock, childComment.getChildren());

            commentBlock.add(childCommentBlock);
        }
    }
    
    private HtmlBlock generateDescriptionDetails() {

        HtmlBlock descriptionDetails = new HtmlBlock("description_details");

        HtmlText date = new HtmlText("description_date", HtmlTools.formatDate(session,demand.getCreationDate()));
        HtmlText author = new HtmlText("description_author", HtmlTools.generateLink(session, demand.getAuthor().getLogin() , new MemberPage(session, demand.getAuthor())));

        

        descriptionDetails.add(author);
        descriptionDetails.add(date);
        

        return descriptionDetails;
    }

    private HtmlBlock generateContributeButton() {
        HtmlForm contributeForm = new HtmlForm(new LogoutAction(session));
        HtmlButton contributeButton = new HtmlButton(session.tr("Contribute"));

        contributeForm.add(contributeButton);

        final HtmlBlock contributeBlock = new HtmlBlock("contribute_block");
        contributeBlock.add(contributeForm);
        return contributeBlock;
    }
    
    private HtmlBlock generateMakeOfferButton() {
        HtmlForm makeOfferForm = new HtmlForm(new LogoutAction(session));
        HtmlButton makeOfferButton = new HtmlButton(session.tr("Make an offer"));

        makeOfferForm.add(makeOfferButton);

        final HtmlBlock makeOfferBlock = new HtmlBlock("make_offer_block");
        makeOfferBlock.add(makeOfferForm);
        return makeOfferBlock;
    }

    private HtmlComponent generateProgressBlock() {

        // block avec la progression
        float progressValue = 0;
        // if(demand.getOffers().size() == 0) {
        progressValue = 42 * (1 - 1 / (1 + demand.getContribution().floatValue() / 200));
        // } else {
        // TODO
        // }


        final HtmlBlock progressBlock = new HtmlBlock("progress_block");
        final HtmlProgressBar progressBar = new HtmlProgressBar(progressValue);

        final HtmlBlock progressBarBlock = new HtmlBlock("column");
        progressBarBlock.add(progressBar);

        progressBlock.add(generateContributeButton());
        progressBlock.add(new HtmlText(demand.getContribution().toPlainString() + "€"));
        progressBlock.add(progressBarBlock);
        progressBlock.add(generateMakeOfferButton());



        return progressBlock;
    }

    private HtmlComponent generateBodyLeft() {
        final HtmlBlock left = new HtmlBlock("leftColumn");

        
        
        left.add(generateTabPane());

        // Comments
        left.add(generateCommentsBlock());
        
        
        return left;

    }

    private HtmlComponent generateBodyRight() {
        final HtmlBlock right = new HtmlBlock("rightColumn");

        HtmlBlock rightBlock = new HtmlBlock("right_block");

        rightBlock.add(generateAbstractBlock());
        
        right.add(rightBlock);

        return right;
    }

    private HtmlComponent generateBody() {

        HtmlBlock demandBody = new HtmlBlock("demand_body");

        demandBody.add(generateBodyLeft());
        demandBody.add(generateBodyRight());

        return demandBody;
    }

    private HtmlComponent generateHead() {
        HtmlBlock demandHead = new HtmlBlock("demand_head");

        HtmlBlock demandHeadKudo = new HtmlBlock("demand_head_kudo");
        HtmlBlock demandHeadProgress = new HtmlBlock("demand_head_progress");
        


        demandHeadKudo.add(generateDemandKudoBlock());
        demandHeadProgress.add(generateProgressBlock());
        
        demandHead.add(demandHeadProgress);
        demandHead.add(demandHeadKudo);
        
        return demandHead;
    }

    private void generateOutputParams() {
        outputParameters.put("id", new Integer(demand.getId()).toString());
        outputParameters.put("title", demand.getTitle());
    }

    private HtmlComponent generateAbstractBlock() {
        HtmlBlock abstractBlock = new HtmlBlock("abstract_block");
        
        abstractBlock.add(generateTimelineBlock());
        //abstractBlock.add(generateContributorsBlock());

        
        return abstractBlock;
    }

    private HtmlComponent generateDemandKudoBlock() {
        HtmlBlock descriptionKudoBlock = new HtmlBlock("description_kudo_block");
        HtmlKudoBox kudoBox = new HtmlKudoBox(demand, session);
        descriptionKudoBlock.add(kudoBox);
        return descriptionKudoBlock;
    }

    private HtmlComponent generateCommentsBlock() {
        HtmlBlock commentsBlock = new HtmlBlock("comments_block");

        commentsBlock.add(new HtmlTitle(session.tr("Comments"), "comments_title"));

        for (Comment comment : demand.getComments()) {
            HtmlBlock commentBlock = new HtmlBlock("main_comment_block");

            String date = "<span class=\"comment_date\">"+HtmlTools.formatDate(session,comment.getCreationDate())+"</span>";
            String author = "<span class=\"comment_author\">"+HtmlTools.generateLink(session, comment.getAuthor().getLogin() , new MemberPage(session, comment.getAuthor()))+"</span>";


            commentBlock.add(new HtmlText(comment.getText()+" – "+ author+" "+date));

            generateChildComment(commentBlock, comment.getChildren());

            commentsBlock.add(commentBlock);

        }
        return commentsBlock;
    }

    private HtmlComponent generateTabPane() {
        HtmlTabBlock tabPane = new HtmlTabBlock("demand_tab");


        HtmlTabBlock.HtmlTab descriptionTab = new HtmlTabBlock.HtmlTab("description_tab", session.tr("Description") ,this, generateDescription());
        HtmlTabBlock.HtmlTab commentTab = new HtmlTabBlock.HtmlTab("comment_tab", session.tr("Comments") ,this, generateCommentsBlock());
        HtmlTabBlock.HtmlTab participationsTab = new HtmlTabBlock.HtmlTab("participations_tab", session.tr("Participations") ,this, generateContributorsBlock());


        tabPane.addTab(descriptionTab);
        tabPane.addTab(commentTab);
        tabPane.addTab(participationsTab);


        if(parameters.containsKey("demand_tab_key")) {
            tabPane.selectTab(parameters.get("demand_tab_key"));
        } else {
            tabPane.selectTab("description_tab");
        }
        



        return tabPane;
    }

}
