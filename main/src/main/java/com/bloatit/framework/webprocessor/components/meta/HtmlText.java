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
package com.bloatit.framework.webprocessor.components.meta;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.web.HtmlTools;

public class HtmlText extends HtmlNode {
    protected String content;

    private HtmlText(final HtmlText text) {
        super();
        this.content = text.content;
    }

    @Override
    public HtmlText clone() {
        return new HtmlText(this);
    }

    /**
     * Creates a component to add raw Html to a page
     * 
     * @param content the Html string to add
     */
    public HtmlText(final String content) {
        super();
        if (content == null) {
            throw new NonOptionalParameterException();
        }
        this.content = HtmlTools.escape(content);
    }

    protected HtmlText(final String content, boolean escapeContent) {
        super();
        if (content == null) {
            throw new NonOptionalParameterException();
        }
        if (escapeContent) {
            this.content = HtmlTools.escape(content);
        } else {
            this.content = content;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Iterator<HtmlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Do not use Only present as a quick hack to write a tad cleaner html
     * content
     */
    public String _getContent() {
        return content;
    }

    @Override
    public final void write(OutputStream out) throws IOException {
        out.write(content.getBytes());
    }

    @Override
    protected boolean selfClosable() {
        return false;
    }
}
