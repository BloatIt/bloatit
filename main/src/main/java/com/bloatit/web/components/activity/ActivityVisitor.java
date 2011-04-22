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
