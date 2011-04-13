package com.bloatit.web.pages.master;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.web.url.AddSoftwarePageUrl;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.SpecialsPageUrl;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");

        final HtmlDiv linkBlock = new HtmlDiv("link_block", "link_block");
        {
            linkBlock.add(new HtmlDiv("footer_link").add(new IndexPageUrl().getHtmlLink(Context.tr("Home page"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new SpecialsPageUrl().getHtmlLink(Context.tr("All pages"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new CreateFeaturePageUrl().getHtmlLink(Context.tr("Create a feature"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new AddSoftwarePageUrl().getHtmlLink(Context.tr("Add a software"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new ChangeLanguagePageUrl().getHtmlLink(Context.tr("Change language"))));
            // linkBlock.add(new HtmlDiv("footer_link").add(new
            // TeamsPageUrl().getHtmlLink(Context.tr("Teams"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new MembersListPageUrl().getHtmlLink(Context.tr("Members"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new SoftwareListPageUrl().getHtmlLink(Context.tr("Software list"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new MetaBugsListPageUrl().getHtmlLink(Context.tr("Signal a bug"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Contacts"))));

        }
        add(linkBlock);

        final HtmlDiv licenceBlock = new HtmlDiv("licence_block", "licence_block");
        {
            licenceBlock.addText(Context.tr("This website use Bloatit framework, and is under GNU Affero Public Licence."));
        }
        add(licenceBlock);
    }

}
