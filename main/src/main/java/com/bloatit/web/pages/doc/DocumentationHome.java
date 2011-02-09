package com.bloatit.web.pages.doc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.renderer.HtmlMarkdownRenderer;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.DocumentationHomeUrl;

@ParamContainer("documentation")
public class DocumentationHome extends MasterPage {
    private final static String DOC_TARGET = "doc";
    private final static String ELVEOS_DOC_DIR = "/home/yoann/elveos/internal/doc/";

    @RequestParam(name = DOC_TARGET, level = Level.ERROR, defaultValue = "home")
    private final String docTarget;

    public DocumentationHome(DocumentationHomeUrl url) {
        super(url);
        docTarget = url.getDocTarget();
    }

    @Override
    protected void doCreate() throws RedirectException {
        HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Elveos documentation home"), 1);
        add(master);

        FileInputStream fis;
        try {
            fis = new FileInputStream(ELVEOS_DOC_DIR + docTarget);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            HtmlMarkdownRenderer content = new HtmlMarkdownRenderer(new String(b));
            master.add(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Welcome to the elveos documentation page");
    }
}
