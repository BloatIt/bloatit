package com.bloatit.web.pages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.linkable.documentation.HtmlDocumentationRenderer;
import com.bloatit.web.linkable.documentation.HtmlDocumentationRenderer.DocumentationType;
import com.bloatit.web.pages.master.BoxLayout;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.DocumentationPageUrl;

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
    private final DocumentationPageUrl url;

    public DocumentationPage(final DocumentationPageUrl url) {
        super(url);
        this.url = url;
        docTarget = url.getDocTarget();
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {

        final TwoColumnLayout layout = new TwoColumnLayout(url);

        final BoxLayout box = new BoxLayout();

        final HtmlDocumentationRenderer docRenderer = new HtmlDocumentationRenderer(DocumentationType.MAIN_DOC, docTarget);
        if (!docRenderer.isExists()) {
            throw new PageNotFoundException();
        }

        box.add(docRenderer);
        layout.addLeft(box);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Elveos documentation: {0}", docTarget);
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        if (docTarget.equals(DEFAULT_DOC)) {
            return DocumentationPage.generateBreadcrumb();
        }

        return DocumentationPage.generateBreadcrumbPage(docTarget);
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new DocumentationPageUrl().getHtmlLink(tr("Documentation")));
        return breadcrumb;
    }

    public static Breadcrumb generateBreadcrumbPage(final String docTarget) {
        final Breadcrumb breadcrumb = DocumentationPage.generateBreadcrumb();

        final DocumentationPageUrl documentationPageUrl = new DocumentationPageUrl();
        documentationPageUrl.setDocTarget(docTarget);
        breadcrumb.pushLink(documentationPageUrl.getHtmlLink(docTarget));

        return breadcrumb;
    }
}
