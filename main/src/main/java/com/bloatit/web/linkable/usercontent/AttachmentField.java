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
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.url.UserContentActionUrl;

public class AttachmentField extends PlaceHolderElement {

    private HtmlFileInput attachedFileInput;
    private HtmlTextField attachmentDescriptionInput;

    public AttachmentField(final UserContentActionUrl targetUrl, final String size, boolean multiple) {
        this(targetUrl, size, multiple, true);
    }

    public AttachmentField(final UserContentActionUrl targetUrl, final String size) {
        this(targetUrl, size, false, true);
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
    public AttachmentField(final UserContentActionUrl targetUrl, String size, final boolean multiple, final boolean jsAutoHide) {
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

        if (multiple) {
            HtmlParagraph multiplePara = new HtmlParagraph(Context.tr("You will be able to add more attachement later."));
            attachmentDiv.add(multiplePara);
        }

        attachedFileInput = new HtmlFileInput(targetUrl.getAttachmentParameter().getName());
        attachedFileInput.addAttribute("size", "10");
        attachedFileInput.setComment(Context.tr("Max size is {0}.", size));
        attachmentDiv.add(attachedFileInput);

        attachmentDescriptionInput = new HtmlTextField(targetUrl.getAttachmentDescriptionParameter().getName());
        attachmentDiv.add(attachmentDescriptionInput);

        if (jsAutoHide) {
            showHide.addListener(attachmentDiv);
            showHide.apply();
        }
    }

    public HtmlFileInput getFileInput() {
        return attachedFileInput;
    }

    public HtmlTextField getTextInput() {
        return attachmentDescriptionInput;
    }
}
