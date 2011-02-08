package com.bloatit.web.pages;

import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.FileResourceUrl;
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


        plop.add(new FileResourceUrl(FileMetadataManager.getFileMetadataById(379)).getHtmlLink("svg"));
    }
}
