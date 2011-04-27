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
package com.bloatit.web.linkable.usercontent;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.url.CreateCommentActionUrl;

public class CreateCommentForm extends HtmlDiv {

    private static final int NB_COLUMNS = 80;
    private static final int NB_ROWS = 10;

    public CreateCommentForm(final CreateCommentActionUrl targetUrl) {
        super("new_comment_block");
        final HtmlForm form = new HtmlForm(targetUrl.urlString());
        add(form);

        final FieldData inputData = targetUrl.getCommentParameter().pickFieldData();
        final HtmlTextArea commentInput = new HtmlTextArea(inputData.getName(), Context.tr("New comment : "), NB_ROWS, NB_COLUMNS);
        commentInput.setDefaultValue(inputData.getSuggestedValue());
        commentInput.addErrorMessages(inputData.getErrorMessages());

        form.add(commentInput);
        commentInput.setComment(Context.tr("Add a new comment. If you want to reply to a previous comment, use the reply link."));

        // TODO remove dependence to Context.getSession()
        form.add(new AsTeamField(targetUrl,
                                 Context.getSession().getAuthToken().getMember(),
                                 UserTeamRight.TALK,
                                 Context.tr("In the name of"),
                                 Context.tr("Write this comment in the name of this team.")));

        form.enableFileUpload();
        form.add(new AttachmentField(targetUrl, "2 Mio"));

        form.add(new HtmlSubmit(Context.tr("Submit comment")));
    }

}
