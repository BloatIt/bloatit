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
import com.bloatit.data.DaoFollowSoftware;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.model.feature.FeatureList;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.lists.ListBinder;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.DuplicateDataException;
import com.bloatit.model.visitor.ModelClassVisitor;

public final class Software extends Identifiable<DaoSoftware> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoSoftware, Software> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Software doCreate(final DaoSoftware dao) {
            return new Software(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Software create(final DaoSoftware dao) {
        return new MyCreator().create(dao);
    }

    private Software(final DaoSoftware id) {
        super(id);
    }

    /**
     * Create a new software. The right management for creating a feature is
     * specific. (The Right management system is not working in this case). You
     * have to use the {@link FeatureManager#canCreate(AuthToken)} to make sure
     * you can create a new feature.
     * @throws UniqueNameExpectedException 
     */
    public Software(final String name, final Member author, final Locale locale, final String description) throws UniqueNameExpectedException {
        this(DaoSoftware.createAndPersist(name, DaoDescription.createAndPersist(author.getDao(), null, Language.fromLocale(locale), " ", description)));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see com.bloatit.data.DaoSoftware#getName()
     */
    public String getName() {
        return getDao().getName();
    }

    /**
     * @see com.bloatit.data.DaoSoftware#getDescription()
     */
    public final Description getDescription() {
        return Description.create(getDao().getDescription());
    }

    /**
     * @see com.bloatit.data.DaoSoftware#getImage()
     */
    public final FileMetadata getImage() {
        return FileMetadata.create(getDao().getImage());
    }

    /**
     * @see com.bloatit.data.DaoSoftware#getFeatures()
     */
    public final FeatureList getFeatures() {
        return new FeatureList(getDao().getFeatures());
    }
    
    public FeatureList getFeaturesByCreationDate() {
        return new FeatureList(getDao().getFeaturesByCreationDate());
    }
    
    public PageIterable<FollowSoftware> getFollowers() {
        return new ListBinder<FollowSoftware, DaoFollowSoftware>(getDao().getFollowers());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public void setImage(final FileMetadata fileImage) {
        getDao().setImage(fileImage.getDao());
    }

    public void setName(final String name) throws DuplicateDataException {
        if (SoftwareManager.nameExists(name)) {
            throw new DuplicateDataException(Action.WRITE);
        }
        getDao().setName(name);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    
}
