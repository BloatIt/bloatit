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
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.CreateCommentAction;
import com.bloatit.web.url.CreateCommentActionUrl;

public class CommentForm extends HtmlDiv {

    private static final int NB_COLUMNS = 80;
    private static final int NB_ROWS = 10;
    public static final int FILE_MAX_SIZE_MIO = 2;

    public CommentForm(final CreateCommentActionUrl targetUrl) {
        super("new_comment_block");
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString(), false);
        add(form);

        final FormBuilder ftool = new FormBuilder(CreateCommentAction.class, targetUrl);
        ftool.add(form, new HtmlTextArea(targetUrl.getCommentParameter().getName(), NB_ROWS, NB_COLUMNS));

        form.addAsTeamField(new AsTeamField(targetUrl,
                                            AuthToken.getMember(),
                                            UserTeamRight.TALK,
                                            Context.tr("In the name of"),
                                            Context.tr("Write this comment in the name of this team.")));

        form.enableFileUpload();
        form.addSubmit(new HtmlSubmit(Context.tr("Submit comment")));
    }

}
