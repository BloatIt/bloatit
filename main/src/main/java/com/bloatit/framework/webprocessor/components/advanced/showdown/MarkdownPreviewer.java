//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.components.advanced.showdown;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;
import com.bloatit.framework.webprocessor.context.Context;

public class MarkdownPreviewer extends HtmlLeaf {
    private final HtmlDiv output;

    public MarkdownPreviewer(final MarkdownEditor source) {
        final HtmlDiv previewer = new HtmlDiv("md_previewer");
        final HtmlSpan mdTitle = new HtmlSpan("title");
        previewer.add(mdTitle);

        mdTitle.addText(Context.tr("Markdown preview"));
        add(previewer);

        this.output = new HtmlDiv("md_preview");

        previewer.add(output);
        final String id = "blmdprev-" + RandomStringUtils.randomAlphabetic(10);
        output.setId(id);
        final HtmlGenericElement script = new HtmlGenericElement("script");

        script.add(new HtmlText("setup_wmd({ input: \"" + source.getInputId() + "\", button_bar: \"" + source.getButtonBarId() + "\", preview: \""
                + output.getId() + "\", output: \"copy_html\" });"));
        previewer.add(script);
    }
}
