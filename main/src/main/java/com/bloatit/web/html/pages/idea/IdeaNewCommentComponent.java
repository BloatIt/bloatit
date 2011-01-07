package com.bloatit.web.html.pages.idea;

import com.bloatit.framework.Demand;
import com.bloatit.web.actions.IdeaCommentAction;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.IdeaCommentActionUrl;

public class IdeaNewCommentComponent extends HtmlPageComponent {
	private Demand targetIdea;

	public IdeaNewCommentComponent(Demand targetIdea) {
        super();
        this.targetIdea = targetIdea;
        add(produce());
    }

    protected HtmlElement produce() {
    	final IdeaCommentActionUrl url = new IdeaCommentActionUrl(targetIdea);
        final HtmlDiv commentBlock = new HtmlDiv("new_comment_block");
        
        HtmlForm form = new HtmlForm(url.urlString());
        commentBlock.add(form);
        
        HtmlTextArea commentInput = new HtmlTextArea(IdeaCommentAction.COMMENT_CONTENT_CODE, Context.tr("Comment : "), 10, 80);
        form.add(commentInput);
        
        form.add(new HtmlSubmit(Context.tr("Submit comment")));
        
        return commentBlock;
    }
}
