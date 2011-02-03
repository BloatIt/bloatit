package com.bloatit.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.bloatit.framework.exceptions.NonOptionalParameterException;

@Entity
public class DaoProject extends DaoIdentifiable {

    @Column(nullable = false, unique = true, updatable = false)
    private String name;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DaoDescription description;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DaoFileMetadata image;

    @OneToMany(mappedBy = "project")
    private final Set<DaoDemand> demands = new HashSet<DaoDemand>();

    public static DaoProject createAndPersist(String name, DaoDescription description, DaoFileMetadata image) {
        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        final DaoProject project = new DaoProject(name, description, image);
        try {
            session.save(project);
        } catch (final HibernateException e) {
            session.getTransaction().rollback();
            session.beginTransaction();
            throw e;
        }
        return project;
    }

    private DaoProject(String name, DaoDescription description, DaoFileMetadata image) {
        super();
        if (name == null || name.isEmpty() || description == null || image == null) {
            throw new NonOptionalParameterException();
        }
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public static DaoProject getByName(String name) {
        Query query = SessionManager.createQuery("from DaoProject where name = :name").setString("name", name);
        return (DaoProject) query.uniqueResult();
    }

    protected void addDemand(DaoDemand demand) {
        demands.add(demand);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DaoProject other = (DaoProject) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }

        return true;
    }

    /**
     * @return the description
     */
    public final DaoDescription getDescription() {
        return description;
    }

    /**
     * @return the image
     */
    public final DaoFileMetadata getImage() {
        return image;
    }

    /**
     * @return the demands
     */
    public final Set<DaoDemand> getDemands() {
        return demands;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    protected DaoProject() {
        super();
    }

}
