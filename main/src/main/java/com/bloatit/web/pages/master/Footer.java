package com.bloatit.web.pages.master;

import com.bloatit.common.CommonConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.web.url.ChangeLanguagePageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.MetaBugsListPageUrl;
import com.bloatit.web.url.SiteMapPageUrl;

public class Footer extends HtmlDiv {

    protected Footer() {
        super();
        setId("footer");

        final HtmlDiv footerBlock = new HtmlDiv("footer_block", "footer_block");

        final HtmlDiv linkBlock = new HtmlDiv("link_block", "link_block");
        {

            linkBlock.add(new HtmlDiv("footer_link").add(new SiteMapPageUrl().getHtmlLink(Context.tr("Site map"))));
            linkBlock.add(new HtmlDiv("footer_link").add(new ChangeLanguagePageUrl().getHtmlLink(Context.tr("Change language"))));
            addDocumentationFooterLink(linkBlock, "contribute", Context.tr("Contribute"));
            linkBlock.add(new HtmlDiv("footer_link").add(new MetaBugsListPageUrl().getHtmlLink(Context.tr("Report a bug"))));
            addDocumentationFooterLink(linkBlock, "api", Context.tr("APIs"));
            addDocumentationFooterLink(linkBlock, "contacts", Context.tr("Contacts"));
            addDocumentationFooterLink(linkBlock, "jobs", Context.tr("Jobs"));
            linkBlock.add(new HtmlDiv("footer_link").add(new UrlString("http://blog.elveos.org").getHtmlLink(Context.tr("Blog & news"))));

        }
        footerBlock.add(linkBlock);

        final HtmlDiv licenceBlock = new HtmlDiv("licence_block", "licence_block");
        {

            licenceBlock.add(new HtmlDiv("agpl_block").add(new HtmlMixedText(Context.tr("This website use Bloatit framework, and is under <0::GNU Affero General Public Licence v3>."),
                                                                             new UrlString("http://www.gnu.org/licenses/agpl.html").getHtmlLink())));
            licenceBlock.add(new HtmlDiv("source_block").add(new HtmlMixedText(Context.tr("The source code of elveos is available on gitorious.org, in the <0:: Bloatit public repository>. The current version is {0}.",
                                                                                          CommonConfiguration.getProjectVersion()),
                                                                               new UrlString("https://gitorious.org/bloatit").getHtmlLink())));
            licenceBlock.add(new HtmlDiv("ccby_block").add(new HtmlMixedText(Context.tr("Content is available under the Creative Commons Attribution (<0::CC-by>) License."),
                                                                             new UrlString("http://creativecommons.org/licenses/by/2.0/").getHtmlLink())));
            licenceBlock.add(new HtmlDiv("linkeos_block").add(new HtmlMixedText(Context.tr("Elveos, elveos.org and the elveos logo are registered trademark of the <0::Linkeos> SAS."),
                                                                                new UrlString("http://linkeos.com").getHtmlLink())));
        }
        footerBlock.add(licenceBlock);

        add(footerBlock);
    }

    private void addDocumentationFooterLink(final HtmlDiv linkBlock, final String docPage, final String label) {
        final DocumentationPageUrl doc = new DocumentationPageUrl();
        doc.setDocTarget(docPage);
        linkBlock.add(new HtmlDiv("footer_link").add(doc.getHtmlLink(label)));
    }

}
