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
                                 Context.getSession().getAuthToken() != null ? Context.getSession().getAuthToken().getMember() : null,
                                 UserTeamRight.TALK,
                                 Context.tr("In the name of"),
                                 Context.tr("Write this comment in the name of this team.")));

        form.enableFileUpload();
        form.add(new AttachmentField(targetUrl,
                                     Context.tr("Join a file"),
                                     Context.tr("You have to add a description if you join a file."),
                                     Context.tr("Describe the file"),
                                     Context.tr("If you join a file you have to describe it.")));

        form.add(new HtmlSubmit(Context.tr("Submit comment")));
    }

}
