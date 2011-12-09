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
package com.bloatit.framework.webprocessor.components.renderer;

import org.springframework.web.util.HtmlUtils;

import com.bloatit.framework.utils.cache.MemoryCache;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlNonEscapedText;
import com.github.rjeschke.txtmark.Processor;

public class HtmlCachedMarkdownRenderer extends HtmlDiv {
    /**
     * Creates a new MarkdownRenderer based on markdown or html text
     *
     * @param text the content to display, must be markdown text if
     *            <code>alreadyRenderer</code> is <code>true</code> or html text
     *            if <code>alreadyRenderer</code> is <code>false</code>
     */
    public HtmlCachedMarkdownRenderer(String text) {
        super("markdown_block");

        text = HtmlUtils.htmlEscape(text);

        final String cached = MemoryCache.getInstance().get(text);
        if (cached == null) {
            final String renderered = Processor.process(text);
            MemoryCache.getInstance().cache(text, renderered);
            add(new HtmlNonEscapedText(renderered));
        } else {
            add(new HtmlNonEscapedText(cached));
        }
    }
}
