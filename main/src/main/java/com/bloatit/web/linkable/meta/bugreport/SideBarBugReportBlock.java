package com.bloatit.web.linkable.meta.bugreport;

import static com.bloatit.framework.webprocessor.context.Context.tr;

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

        bugDetail.add(new HtmlMixedText(tr("You can use the <0::bug report> system to report any problem on elveos.org website."),
                                        new MetaBugsListPageUrl().getHtmlLink()));

        add(bugDetail);

        final MetaReportBugActionUrl reportBugActionUrl = new MetaReportBugActionUrl();
        final HtmlForm form = new HtmlForm(reportBugActionUrl.urlString());
        final HtmlHidden hiddenUrl = new HtmlHidden(MetaReportBugAction.BUG_URL, currentUrl.urlString());
        form.add(hiddenUrl);

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
