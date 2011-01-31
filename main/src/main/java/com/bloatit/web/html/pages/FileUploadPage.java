package com.bloatit.web.html.pages;

import org.w3c.dom.html.HTMLInputElement;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.html.components.standard.form.HtmlFileInput;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.FileUploadPageUrl;
import com.bloatit.web.utils.url.IndexPageUrl;

@ParamContainer("file/upload")
public class FileUploadPage extends Page{

    private FileUploadPageUrl url;

    public FileUploadPage(FileUploadPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String getTitle() {
        return "Upload a file";
    }

    @Override
    public boolean isStable() {
        return false;
    }
    
    public void doCreate() {
        HtmlForm plop =new HtmlForm(new IndexPageUrl().urlString()); 
        add(plop);
        
        plop.addAttribute("enctype","multipart/form-data");
        
        HtmlTextField htf = new HtmlTextField("wow");
        plop.add(htf);
        
        HtmlFileInput hfi = new HtmlFileInput("fichier");
        plop.add(hfi);
        plop.add(new HtmlSubmit("Send"));
    }
}
