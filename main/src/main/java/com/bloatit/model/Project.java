package com.bloatit.model;

import java.util.Set;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoGroup;
import com.bloatit.data.DaoProject;

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
     * @return
     * @see com.bloatit.data.DaoProject#getOwner()
     */
    public final DaoGroup getOwner() {
        return getDao().getOwner();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoProject#getDescription()
     */
    public final DaoDescription getDescription() {
        return getDao().getDescription();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoProject#getImage()
     */
    public final DaoFileMetadata getImage() {
        return getDao().getImage();
    }

    /**
     * @return
     * @see com.bloatit.data.DaoProject#getDemands()
     */
    public final Set<DaoDemand> getDemands() {
        return getDao().getDemands();
    }

}
