/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.html;

/**
 * Class to describe HtmlContainers
 */
public abstract class HtmlBranch extends HtmlElement {

    protected HtmlBranch() {
        super();
    }

    public HtmlBranch(final String tag) {
        super(tag);
    }

    @Override
    public final HtmlBranch add(final HtmlNode html) {
        super.add(html);
        return this;
    }

    @Override
    public final HtmlBranch addText(final String text) {
        super.addText(text);
        return this;
    }

    @Override
    public HtmlBranch addAttribute(final String name, final String value) {
        super.addAttribute(name, value);
        return this;
    }

    @Override
    public final HtmlBranch setCssClass(final String cssClass) {
        super.setCssClass(cssClass);
        return this;
    }

    @Override
    public final HtmlBranch setId(final String id) {
        super.setId(id);
        return this;
    }

    @Override
    public boolean selfClosable() {
        return false;
    }
}