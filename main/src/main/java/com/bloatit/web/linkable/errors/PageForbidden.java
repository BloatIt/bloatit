package com.bloatit.web.linkable.errors;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.xcgiserver.HttpResponse.StatusCode;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.IndexPageUrl;

public class PageForbidden extends ElveosPage {

    private final PageForbiddenUrl url;

    public PageForbidden(final PageForbiddenUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent(final ElveosUserToken userToken) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final HtmlDiv box = new HtmlDiv("forbidden_error");
        layout.addLeft(box);

        final HtmlTitle errorTitle = new HtmlTitle(Context.tr("Forbidden error"), 1);
        box.add(errorTitle);

        final HtmlParagraph error = new HtmlParagraph();
        final String tr = Context.tr("The page you are looking for is forbidden. Maybe you can try heading back to the <0::Home page>.");
        final HtmlMixedText mixed = new HtmlMixedText(tr, new IndexPageUrl().getHtmlLink());
        error.add(mixed);
        box.add(error);

        final HtmlParagraph bug = new HtmlParagraph();
        final String tr2 = Context.tr("You most likely ended up here because of a bug, please report it using the box on the right of the screen.");
        final HtmlMixedText mixedExplain = new HtmlMixedText(tr2, new IndexPageUrl().getHtmlLink());
        bug.add(mixedExplain);
        box.add(bug);

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return tr("Forbidden page");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final ElveosUserToken userToken) {
        return PageForbidden.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new PageForbiddenUrl().getHtmlLink(tr("Forbidden")));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected StatusCode getResponseStatus() {
        return StatusCode.ERROR_403_FORBIDDEN;
    }

}
