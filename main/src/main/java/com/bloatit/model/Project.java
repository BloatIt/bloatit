package com.bloatit.model;

import java.util.Locale;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoProject;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.demand.DemandList;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.right.ProjectRight;
import com.bloatit.model.right.RightManager.Action;

public class Project extends Identifiable<DaoProject> {

    public static Project create(final DaoProject dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoProject> created = CacheManager.get(dao);
            if (created == null) {
                return new Project(dao);
            }
            return (Project) created;
        }
        return null;
    }

    private Project(DaoProject id) {
        super(id);
    }

    /**
     * Create a new project. The right management for creating a demand is specific. (The
     * Right management system is not working in this case). You have to use the
     * {@link DemandManager#canCreate(AuthToken)} to make sure you can create a new
     * demand.
     *
     * @see DaoDemand#DaoDemand(Member,Locale,String, String)
     */
    public Project(String name, final Member author, final Locale locale, final String title, final String description, FileMetadata image) {
        this(DaoProject.createAndPersist(name, DaoDescription.createAndPersist(author.getDao(), locale, title, description),
                image.getDao()));
    }

    /**
     * @return
     * @see com.bloatit.data.DaoProject#getName()
     */
    public String getName() throws UnauthorizedOperationException {
        new ProjectRight.Name().tryAccess(calculateNoOwnerRole(), Action.READ);
        return getDao().getName();
    }


    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getDescription()
     */
    public final Description getDescription() throws UnauthorizedOperationException {
        new ProjectRight.Name().tryAccess(calculateNoOwnerRole(), Action.READ);
        return Description.create(getDao().getDescription());
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getImage()
     */
    public final FileMetadata getImage() throws UnauthorizedOperationException {
        new ProjectRight.Name().tryAccess(calculateNoOwnerRole(), Action.READ);
        return FileMetadata.create(getDao().getImage());
    }

    /**
     * @return
     * @throws UnauthorizedOperationException
     * @see com.bloatit.data.DaoProject#getDemands()
     */
    public final DemandList getDemands() throws UnauthorizedOperationException {
        new ProjectRight.Name().tryAccess(calculateNoOwnerRole(), Action.READ);
        // TODO: implement
        return null;
    }

}
