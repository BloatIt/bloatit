package com.bloatit.framework.webprocessor.components.advanced.showdown;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.components.meta.XmlText;
import com.bloatit.framework.webprocessor.context.Context;

public class MarkdownPreviewer extends HtmlLeaf {
    private final HtmlDiv output;

    public MarkdownPreviewer(final MarkdownEditor source) {
        HtmlDiv previewer = new HtmlDiv("md_previewer");
        HtmlSpan mdTitle = new HtmlSpan("title");
        previewer.add(mdTitle);

        mdTitle.addText(Context.tr("Markdown preview"));
        add(previewer);

        this.output = new HtmlDiv("md_preview");

        previewer.add(output);
        final String id = "blmdprev-" + RandomStringUtils.randomAlphabetic(10);
        output.setId(id);
        final HtmlGenericElement script = new HtmlGenericElement("script");

        script.add(new XmlText("setup_wmd({ input: \"" + source.getInputId() + "\", button_bar: \"" + source.getButtonBarId() + "\", preview: \""
                + output.getId() + "\", output: \"copy_html\" });"));
        previewer.add(script);
    }
}
