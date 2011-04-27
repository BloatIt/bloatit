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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.webprocessor.components.writers.QueryResponseStream;

public class XmlText extends XmlNode {
    protected String content;

    protected XmlText() {
        super();
    }

    private XmlText(final XmlText text) {
        super();
        this.content = text.content;
    }

    @Override
    public XmlText clone() {
        return new XmlText(this);
    }

    /**
     * Creates a component to add raw Html to a page
     * 
     * @param content the Html string to add
     */
    public XmlText(final String content) {
        super();
        if (content == null) {
            throw new NonOptionalParameterException();
        }
        this.content = content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<XmlNode> iterator() {
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
    public final void write(final QueryResponseStream txt) {
        txt.writeRawText(content);
    }

    @Override
    protected final List<String> getCustomCss() {
        return null;
    }

    @Override
    protected final List<String> getCustomJs() {
        return null;
    }

}
