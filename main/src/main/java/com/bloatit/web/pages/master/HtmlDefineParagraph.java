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
package com.bloatit.web.pages.master;

import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;

public class HtmlDefineParagraph extends HtmlParagraph {

    public HtmlDefineParagraph(final String key, final String text) {
        setCssClass("define_p");
        add(new HtmlSpan("define_key").addText(key));
        addText(text);
    }

    public HtmlDefineParagraph(final String key, final HtmlNode body) {
        setCssClass("define_p");

        add(new HtmlSpan("define_key").addText(key));
        add(body);

    }

    public HtmlNode addCssClass(final String css) {
        setCssClass("define_p " + css);
        return this;
    }
}
