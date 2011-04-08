package com.bloatit.web.pages;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.UploadFileActionUrl;

@ParamContainer("file/upload")
public class FileUploadPage extends MasterPage {

    public FileUploadPage(final FileUploadPageUrl url) {
        super(url);
    }

    @Override
    protected String getPageTitle() {
        return "Upload a file";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public void doCreate() {
        final HtmlForm plop = new HtmlForm(new UploadFileActionUrl().urlString());
        add(new HtmlDiv("padding_box").add(plop));

        plop.addAttribute("enctype", "multipart/form-data");

        final HtmlTextField htf = new HtmlTextField("wow");
        plop.add(htf);

        final HtmlFileInput hfi = new HtmlFileInput("fichier");
        plop.add(hfi);
        plop.add(new HtmlSubmit("Send"));

    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return FileUploadPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new FileUploadPageUrl().getHtmlLink(tr("File upload")));

        return breadcrumb;
    }

}
