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
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoProject;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.feature.FeatureList;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.ProjectRight;

public class Project extends Identifiable<DaoProject> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoProject, Project> {
        @Override
        public Project doCreate(final DaoProject dao) {
            return new Project(dao);
        }
    }

    public static Project create(final DaoProject dao) {
        return new MyCreator().create(dao);
    }

    private Project(final DaoProject id) {
        super(id);
    }

    /**
     * Create a new project. The right management for creating a feature is
     * specific. (The Right management system is not working in this case). You
     * have to use the {@link FeatureManager#canCreate(AuthToken)} to make sure
     * you can create a new feature.
     *
     * @see DaoFeature#DaoFeature(Member,Locale,String, String)
     */
    public Project(final String name, final Member author, final Locale locale, final String title, final String description) {
        this(DaoProject.createAndPersist(name, DaoDescription.createAndPersist(author.getDao(), locale, title, description)));
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     * @see com.bloatit.data.DaoProject#getName()
     */
    public String getName() throws UnauthorizedOperationException {
        tryAccess(new ProjectRight.Name(), Action.READ);
        return getDao().getName();
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getDescription()
     */
    public final Description getDescription() throws UnauthorizedOperationException {
        tryAccess(new ProjectRight.Name(), Action.READ);
        return Description.create(getDao().getDescription());
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getImage()
     */
    public final FileMetadata getImage() throws UnauthorizedOperationException {
        tryAccess(new ProjectRight.Name(), Action.READ);
        return FileMetadata.create(getDao().getImage());
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getFeatures()
     */
    public final FeatureList getFeatures() throws UnauthorizedOperationException {
        tryAccess(new ProjectRight.Name(), Action.READ);
        return new FeatureList(getDao().getFeatures());

    }

    public void setImage(FileMetadata fileImage) {
        // TODO: right management
        getDao().setImage(fileImage.getDao());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // RightManagement
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected boolean isMine(final Member member) {
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
