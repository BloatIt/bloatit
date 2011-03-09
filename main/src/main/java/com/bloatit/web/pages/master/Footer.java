package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.url.AddProjectPageUrl;
import com.bloatit.web.url.CreateDemandPageUrl;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.ProjectListPageUrl;
import com.bloatit.web.url.SpecialsPageUrl;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");

        final HtmlDiv linkBlock = new HtmlDiv("link_block", "link_block");
        {
            linkBlock.add(new HtmlDiv("footer_link").add(new IndexPageUrl().getHtmlLink(Context.tr("Home page"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new SpecialsPageUrl().getHtmlLink(Context.tr("All pages"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new FileUploadPageUrl().getHtmlLink(Context.tr("Upload file"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new CreateDemandPageUrl().getHtmlLink(Context.tr("Create a demand"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new AddProjectPageUrl().getHtmlLink(Context.tr("Add a project"))));
            // linkBlock.add(new HtmlDiv("footer_link").add(new
            // TeamsPageUrl().getHtmlLink(Context.tr("Teams"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new MembersListPageUrl().getHtmlLink(Context.tr("Members"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new ProjectListPageUrl().getHtmlLink(Context.tr("Project list"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Signal a bug"))));
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
