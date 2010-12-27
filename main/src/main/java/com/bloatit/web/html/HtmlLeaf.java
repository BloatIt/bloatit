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
 * Represents the leaves of the html tree : no other element can be added inside
 */
public abstract class HtmlLeaf extends HtmlElement {

    public HtmlLeaf(final String tag) {
        super(tag);
    }

    public HtmlLeaf() {
        super();
    }

    @Override
    public boolean selfClosable() {
        return true;
    }
}
