package com.bloatit.web.pages;

import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.FileUploadPageUrl;
import com.bloatit.web.url.IndexPageUrl;

@ParamContainer("file/upload")
public class FileUploadPage extends Page {

    public FileUploadPage(final FileUploadPageUrl url) {
        super(url);
    }

    @Override
    protected String getTitle() {
        return "Upload a file";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public void doCreate() {
        final HtmlForm plop = new HtmlForm(new IndexPageUrl().urlString());
        add(plop);

        plop.addAttribute("enctype", "multipart/form-data");

        final HtmlTextField htf = new HtmlTextField("wow");
        plop.add(htf);

        final HtmlFileInput hfi = new HtmlFileInput("fichier");
        plop.add(hfi);
        plop.add(new HtmlSubmit("Send"));
    }
}
