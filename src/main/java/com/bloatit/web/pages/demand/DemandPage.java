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

import com.bloatit.web.actions.OfferAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlImage;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlRenderer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Comment;
import com.bloatit.framework.Contribution;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Offer;
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
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTabBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.pages.ContributePage;
import com.bloatit.web.pages.MemberPage;
import com.bloatit.web.pages.OfferPage;
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

    public Demand getDemand() {
        return demand;
    }

    private HtmlBlock generateTimelineBlock() {
        HtmlBlock timelineBlock = new HtmlBlock("timeline_block");

        HtmlList timelineList = new HtmlList();

        HtmlListItem creationDate = new HtmlListItem("Creation: " + demand.getCreationDate().toString());
        timelineList.addItem(creationDate);
        timelineBlock.add(timelineList);

        return timelineBlock;
    }

   
    

    private HtmlBlock generateContributeButton() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("idea", String.valueOf(this.demand.getId()));

        HtmlForm contributeForm = new HtmlForm(new ContributePage(session, params));
        contributeForm.setMethod(HtmlForm.Method.GET);

        HtmlButton contributeButton = new HtmlButton(session.tr("Contribute"));
        contributeForm.add(contributeButton);

        final HtmlBlock contributeBlock = new HtmlBlock("contribute_block");
        contributeBlock.add(contributeForm);
        return contributeBlock;
    }

    private HtmlBlock generateMakeOfferButton() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("idea", String.valueOf(this.demand.getId()));

        HtmlForm makeOfferForm = new HtmlForm(new OfferPage(session, params));
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
        left.add(new DemandTabPane(this));
        // Comments
        left.add(new DemandCommentListComponent(this));
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
        parameters.put("id", new Integer(demand.getId()).toString());
        parameters.put("title", demand.getTitle());
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

}
