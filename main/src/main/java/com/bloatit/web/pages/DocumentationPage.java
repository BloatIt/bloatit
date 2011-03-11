package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.PageNotFoundException;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.web.pages.documentation.HtmlDocumentationRenderer;
import com.bloatit.web.pages.documentation.HtmlDocumentationRenderer.DocumentationType;
import com.bloatit.web.pages.master.BoxLayout;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.DocumentationUrl;

/**
 * <p>
 * A holding class for documentation
 * </p>
 * <p>
 * Documentation system is based on markdown files hosted on the server. This
 * page is a container used to view these markdown documents. <br />
 * Document to display is chosen via the GET parameter Documenvalue
 * tation#DOC_TARGET.
 * </p>
 */
@ParamContainer("documentation")
public class DocumentationPage extends MasterPage {
    private final static String DOC_TARGET = "doc";
    private final static String DEFAULT_DOC = "home";

    @RequestParam(name = DOC_TARGET)
    @Optional(DEFAULT_DOC)
    private final String docTarget;

    public DocumentationPage(final DocumentationUrl url) {
        super(url);
        docTarget = url.getDocTarget();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected void doCreate() throws RedirectException {

        TwoColumnLayout layout = new TwoColumnLayout();

        BoxLayout box = new BoxLayout();

        HtmlDocumentationRenderer docRenderer = new HtmlDocumentationRenderer(DocumentationType.MAIN_DOC, docTarget);
        if (!docRenderer.isExists()) {
            throw new PageNotFoundException();
        }

        box.add(docRenderer);
        layout.addLeft(box);

        add(layout);
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Elveos documentation: {0}", docTarget);
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        if(docTarget.equals(DEFAULT_DOC)) {
            return DocumentationPage.generateBreadcrumb();
        }

        return DocumentationPage.generateBreadcrumbPage(docTarget);
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new DocumentationPageUrl().getHtmlLink(tr("Documentation")));

        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbPage(String docTarget) {
        Breadcrumb breadcrumb = DocumentationPage.generateBreadcrumb();

        DocumentationPageUrl documentationPageUrl = new DocumentationPageUrl();
        documentationPageUrl.setDocTarget(docTarget);
        breadcrumb.pushLink(documentationPageUrl.getHtmlLink(docTarget));

        return breadcrumb;
    }

}
