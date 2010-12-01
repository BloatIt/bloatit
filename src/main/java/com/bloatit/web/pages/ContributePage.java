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

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.ContributionAction;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextArea;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Session;

public class ContributePage extends LoggedPage {

    public ContributePage(Session session) {
        this(session, new HashMap<String, String>());
    }

    public ContributePage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    public HtmlComponent generateRestrictedContent() {
        // Checking parameters
        // TODO : do something when id is invalid / there is no id
        int ideaId = -1;
        Demand targetIdea = null;
        if(this.parameters.containsKey("idea")){
            try{
                ideaId = Integer.parseInt(this.parameters.get("idea"));
                targetIdea = DemandManager.getDemandById(ideaId);
            } catch (NumberFormatException nfe){
            }
        }

        if (ideaId == -1 ){
            session.notifyBad(session.tr("You need to choose an idea on which you'll contribute"));
            htmlResult.setRedirect(new DemandsPage(session));
            return null;
        }

        if (targetIdea == null){
            session.notifyBad(session.tr("The idea you chose does not exists (id :"+ideaId+")"));
            htmlResult.setRedirect(new DemandsPage(session));
            return null;
        }

        // Case: OK
        final ContributionAction contribAction = new ContributionAction(session, this.parameters);
        
        final HtmlForm contribForm = new HtmlForm(contribAction);
        contribForm.setMethod(HtmlForm.Method.POST);

        // Input field : chose amount
        final HtmlTextField contribField = new HtmlTextField(session.tr("Choose amount : "));
        
        if(this.parameters.containsKey(contribAction.getContributionCode())){
            contribField.setDefaultValue(this.parameters.get(contribAction.getContributionCode()));
        }
        contribField.setName(contribAction.getContributionCode());
        
        // Input field : comment
        final HtmlTextArea commentField = new HtmlTextArea(session.tr("Comment (optionnal) : "));
        if(this.parameters.containsKey(contribAction.getCommentCode())){
            commentField.setDefaultValue(this.parameters.get(contribAction.getCommentCode()));
        }
        commentField.setName(contribAction.getCommentCode());

        final HtmlButton submitButton = new HtmlButton(session.tr("Contribute"));

        // Summary of the idea
        HtmlTitle summary = new HtmlTitle(targetIdea.getTitle(),"");
        HtmlText textSummary = new HtmlText(targetIdea.getDescription().toString());
        summary.add(textSummary);

        // Create the form
        contribForm.add(contribField);
        contribForm.add(commentField);
        contribForm.add(submitButton);

        final HtmlTitle contribTitle = new HtmlTitle(session.tr("Contribute"), "");
        contribTitle.add(summary);
        contribTitle.add(contribForm);

        final HtmlContainer group = new HtmlContainer();
        group.add(contribTitle);

        return group;
    }

    @Override
    public String getCode() {
        return "contribute";
    }

    @Override
    protected String getTitle() {
        return session.tr("Contribute to a project");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return session.tr("You must be logged to contribute");
    }
}
