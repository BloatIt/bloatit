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

import com.bloatit.framework.Comment;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Transaction;
import com.bloatit.framework.Translation;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
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
                this.demand = DemandManager.GetDemandById(id);
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
        //if(demand.getOffers().size() == 0) {
            progressValue = 100*(1-1/(1+demand.getContribution().floatValue()/200));
        //} else {
            //TODO
        //}


        HtmlForm contributeForm = new HtmlForm(new LoginAction(session));
        HtmlButton contributeButton = new HtmlButton(session.tr("Contribuer"));

        contributeForm.add(contributeButton);

        final HtmlBlock contributeBlock = new HtmlBlock("contribute_block");
        contributeBlock.add(contributeForm);


        final HtmlBlock progressBlock = new HtmlBlock("progress_block");
        final HtmlProgressBar progressBar = new HtmlProgressBar(progressValue);
        
        final HtmlBlock progressBarBlock = new HtmlBlock("column");
        progressBarBlock.add(progressBar);


        progressBlock.add(contributeBlock);
        progressBlock.add(new HtmlText(demand.getContribution().toPlainString()+"€"));
        progressBlock.add(progressBarBlock);



        
        
        
        

        left.add(progressBlock);
        
        
        // Description
        HtmlBlock descriptionBlock = new HtmlBlock("description_block");
        HtmlText description = new HtmlText(translatedDescription.getText());
        descriptionBlock.add(description);
        left.add(descriptionBlock);


        // Comments

        HtmlBlock commentsBlock = new HtmlBlock("comments_block");

        commentsBlock.add(new HtmlTitle(session.tr("Comments"),"comments_title"));

        for(Comment comment: demand.getComments()) {
            HtmlBlock commentBlock = new HtmlBlock("main_comment_block");
            commentBlock.add(new HtmlText(comment.getText()));

            

            for(Comment childComment : comment.getChildren()) {
                HtmlBlock childCommentBlock = new HtmlBlock("child_comment_block");
                childCommentBlock.add(new HtmlText(childComment.getText()));
            }

            commentsBlock.add(commentBlock);

        }

        left.add(commentsBlock);
        
        // droite process
        
       

        return page;

    }

    @Override
    public String getCode() {
        if (this.demand != null) {
            return new HtmlString(session).add("demand/id-" + this.demand.getId() + "/title-").secure(demand.getTitle()).toString();
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
}
