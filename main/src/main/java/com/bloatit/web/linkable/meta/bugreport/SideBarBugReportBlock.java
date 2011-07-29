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
package com.bloatit.web.linkable.meta.bugreport;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlHidden;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.pages.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.MetaReportBugActionUrl;

public class SideBarBugReportBlock extends TitleSideBarElementLayout {

    public SideBarBugReportBlock(final Url currentUrl) {
        setTitle(tr("Bug report"));

        final HtmlParagraph bugDetail = new HtmlParagraph();

        final HtmlLink htmlLink = new MetaBugsListPageUrl().getHtmlLink();
        htmlLink.setNoFollow();
        bugDetail.add(new HtmlMixedText(tr("You can use the <0::bug report> system to report any problem on elveos.org website."), htmlLink));

        add(bugDetail);

        final MetaReportBugActionUrl reportBugActionUrl = new MetaReportBugActionUrl();
        final HtmlForm form = new HtmlForm(reportBugActionUrl.urlString());
        final HtmlHidden hiddenUrl = new HtmlHidden(MetaReportBugAction.BUG_URL, currentUrl.urlString());

        final FieldData descriptionFieldData = reportBugActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea bugDescription = new HtmlTextArea(descriptionFieldData.getName(), 5, 50);
        bugDescription.setDefaultValue(descriptionFieldData.getSuggestedValue());
        bugDescription.setComment(tr("You can use markdown syntax in this field."));

        final HtmlSubmit submit = new HtmlSubmit(tr("Report the bug"));

        form.add(hiddenUrl);
        form.add(bugDescription);
        form.add(submit);
        add(form);

    }
}
