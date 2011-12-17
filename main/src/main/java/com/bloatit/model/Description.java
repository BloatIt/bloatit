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
package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoDescription;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.model.lists.TranslationList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtTranslation;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.visitor.ModelClassVisitor;

/**
 * A description must be created through the Feature class. (For example, you
 * create a description each time you create a feature.) There is no right
 * management for this class. I assume that if you can get a
 * <code>Description</code> then you can access every property in it.
 * 
 * @see DaoDescription
 */
public final class Description extends Identifiable<DaoDescription> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoDescription, Description> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Description doCreate(final DaoDescription dao) {
            return new Description(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Description create(final DaoDescription dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Create a Description. If you are looking for a way to create a new
     * description see
     * {@link Feature#addOffer(java.math.BigDecimal, String, Locale, java.util.Date, int)}
     * 
     * @param member is the author of this description
     * @param locale is the locale in which the description is written.
     * @param title is the title of the description
     * @param description is the main text of the description (the actual
     *            description)
     */
    public Description(final Member member, final Team team, final Language language, final String title, final String description) {
        super(DaoDescription.createAndPersist(member.getDao(), DaoGetter.get(team), language, title, description));
    }

    /**
     * Create a description using its dao representation.
     */
    private Description(final DaoDescription dao) {
        super(dao);
    }

    public void addTranslation(final Member member, final Team team, final Language language, final String title, final String description)
            throws UnauthorizedOperationException {
        tryAccess(new RgtTranslation.Text(), Action.WRITE);
        getDao().addTranslation(member.getDao(), DaoGetter.get(team), language, title, description);
    }

    public void setDefaultLanguage(final Language defaultLanguage) {
        getDao().setDefaultLanguage(defaultLanguage);
    }

    /**
     * @return all the translations for a description and <code>this</code>
     *         also.
     * @see DaoDescription#getTranslations()
     */
    public PageIterable<Translation> getTranslations() {
        return new TranslationList(getDao().getTranslations());
    }

    public Translation getTranslation(final Language language) {
        return Translation.create(getDao().getTranslation(language));
    }

    public Translation getTranslationOrDefault(final Language language) {
        final Translation tr = getTranslation(language);
        if (tr == null) {
            return getTranslation(getDefaultLanguage());
        }
        return tr;
    }

    // private Translation getDefaultTranslation() {
    // return Translation.create(getDao().getDefaultTranslation());
    // }

    public Language getDefaultLanguage() {
        return getDao().getDefaultLanguage();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
