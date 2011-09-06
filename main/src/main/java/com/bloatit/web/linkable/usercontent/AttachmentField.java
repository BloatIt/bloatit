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

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.url.UserContentActionUrl;

public class AttachmentField extends PlaceHolderElement {

    public AttachmentField(final UserContentActionUrl targetUrl, final String size, final boolean jsAutoHide) {
        this(targetUrl,
             Context.tr("Join a file"),
             Context.tr("Max size {0}. When you join a file, you have to add a description.", size),
             Context.tr("File description"),
             Context.tr("Input a short description of the file you want to upload."),
             jsAutoHide);
    }

    public AttachmentField(final UserContentActionUrl targetUrl, final String size) {
        this(targetUrl,
             Context.tr("Join a file"),
             Context.tr("Max size {0}. When you join a file, you have to add a description.", size),
             Context.tr("File description"),
             Context.tr("Input a short description of the file you want to upload."),
             true);
    }

    /**
     * Do not forget the: form.enableFileUpload();
     * 
     * @param targetUrl
     * @param attachmentLabel
     * @param attachmentComment
     * @param descriptionLabel
     * @param descriptionComment
     */
    private AttachmentField(final UserContentActionUrl targetUrl,
                            final String attachmentLabel,
                            final String attachmentComment,
                            final String descriptionLabel,
                            final String descriptionComment,
                            final boolean jsAutoHide) {
        super();

        final JsShowHide showHide = new JsShowHide(this, false);
        showHide.setHasFallback(false);
        if (jsAutoHide) {
            final HtmlParagraph addAttachementLink = new HtmlParagraph(Context.tr("+ add attachement"), "fake_link");
            add(addAttachementLink);
            showHide.addActuator(addAttachementLink);
        }

        final HtmlDiv attachmentDiv = new HtmlDiv();
        add(attachmentDiv);

        final FieldData attachedFileData = targetUrl.getAttachmentParameter().pickFieldData();
        final HtmlFileInput attachedFileInput = new HtmlFileInput(attachedFileData.getName(), attachmentLabel);
        attachedFileInput.setDefaultValue(attachedFileData.getSuggestedValue());
        attachedFileInput.addErrorMessages(attachedFileData.getErrorMessages());
        attachedFileInput.setComment(attachmentComment);
        attachmentDiv.add(attachedFileInput);

        final FieldData attachmentDescriptiondData = targetUrl.getAttachmentDescriptionParameter().pickFieldData();
        final HtmlTextField attachmentDescriptionInput = new HtmlTextField(attachmentDescriptiondData.getName(), descriptionLabel);
        attachmentDescriptionInput.setDefaultValue(attachmentDescriptiondData.getSuggestedValue());
        attachmentDescriptionInput.addErrorMessages(attachmentDescriptiondData.getErrorMessages());
        attachmentDescriptionInput.setComment(descriptionComment);
        attachmentDiv.add(attachmentDescriptionInput);

        if (jsAutoHide) {
            showHide.addListener(attachmentDiv);
            showHide.apply();
        }

    }

}
