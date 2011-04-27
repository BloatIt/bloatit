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
package com.bloatit.web.components.activity;

import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.AbstractModelClassVisitor;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Feature;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Kudos;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.Translation;

public abstract class ActivityVisitor extends AbstractModelClassVisitor<HtmlElement> {
    @Override
    public abstract HtmlElement visit(final Translation model);

    @Override
    public abstract HtmlElement visit(final Kudos model);

    @Override
    public abstract HtmlElement visit(final Contribution model);

    @Override
    public abstract HtmlElement visit(final Feature model);

    @Override
    public abstract HtmlElement visit(final Offer model);

    @Override
    public abstract HtmlElement visit(final Release model);

    @Override
    public abstract HtmlElement visit(final Bug model);

    @Override
    public abstract HtmlElement visit(final FileMetadata model);

    @Override
    public abstract HtmlElement visit(Comment model);
}
