package com.bloatit.web.html.pages.idea;

import com.bloatit.framework.Comment;
import com.bloatit.web.html.components.custom.HtmlKudoBlock;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;

public class CommentKudoComponent extends HtmlPageComponent{

    public CommentKudoComponent(final Comment comment) {
        super();

        final HtmlDiv descriptionKudoBlock = new HtmlDiv("comment_kudo_block");
        {
            final HtmlKudoBlock kudoBox = new HtmlKudoBlock(comment, Context.getSession());
            descriptionKudoBlock.add(kudoBox);
        }
        add(descriptionKudoBlock);
    }
}