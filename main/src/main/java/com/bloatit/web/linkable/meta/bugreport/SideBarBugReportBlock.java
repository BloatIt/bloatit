package com.bloatit.web.linkable.meta.bugreport;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlHidden;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.meta.HtmlMixedText;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.pages.master.TitleSideBarElementLayout;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.MetaReportBugActionUrl;

public class SideBarBugReportBlock extends TitleSideBarElementLayout {

    public SideBarBugReportBlock(Url currentUrl) {
        setTitle(tr("Bug report"));

        HtmlParagraph bugDetail = new HtmlParagraph();

        bugDetail.add(new HtmlMixedText(tr("You can use the <0::bug report> system to report any problem on elveos.org website."), new MetaBugsListPageUrl().getHtmlLink()));

        add(bugDetail);

        MetaReportBugActionUrl reportBugActionUrl = new MetaReportBugActionUrl();
        HtmlForm form = new HtmlForm(reportBugActionUrl.urlString());
        HtmlHidden hiddenUrl = new HtmlHidden(MetaReportBugAction.BUG_URL, currentUrl.urlString());
        form.add(hiddenUrl);

        FieldData descriptionFieldData = reportBugActionUrl.getDescriptionParameter().pickFieldData();
        HtmlTextArea bugDescription = new HtmlTextArea(descriptionFieldData.getName(), 5, 50);
        bugDescription.setDefaultValue(descriptionFieldData.getSuggestedValue());
        bugDescription.setComment(tr("You can use markdown syntax in this field."));

        HtmlSubmit submit = new HtmlSubmit(tr("Report the bug"));

        form.add(hiddenUrl);
        form.add(bugDescription);
        form.add(submit);
        add(form);



    }
}
