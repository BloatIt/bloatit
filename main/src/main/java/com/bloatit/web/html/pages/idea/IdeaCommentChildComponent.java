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

import com.bloatit.framework.Comment;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.HtmlTools;
import com.bloatit.web.html.components.custom.renderer.HtmlRawTextRenderer;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.pages.master.HtmlPageComponent;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;

public class IdeaCommentChildComponent extends HtmlPageComponent {

    // private Offer offer;
    private final Comment comment;

    public IdeaCommentChildComponent(final Comment comment) {
        super();
        this.comment = comment;
        add(produce());

    }

    protected HtmlElement produce() {
        final HtmlDiv commentBlock = new HtmlDiv("child_comment_block");
        {
            final Session session = Context.getSession();
            final HtmlGenericElement date = new HtmlGenericElement("span");
            date.addText(HtmlTools.formatDate(session, comment.getCreationDate()));
            date.setCssClass("comment_date");
            
            final HtmlGenericElement author = new HtmlGenericElement("span");
            author.addText(comment.getAuthor().getLogin());
            author.setCssClass("comment_author");
            
            HtmlRawTextRenderer commentText = new HtmlRawTextRenderer(comment.getText());
            commentBlock.add(commentText);
            commentBlock.add(date);
            commentBlock.add(author);

            for (final Comment childComment : comment.getChildren()) {
                commentBlock.add(new IdeaCommentChildComponent(childComment));
            }
        }
        return commentBlock;
    }
}
