package com.bloatit.data;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.utils.PageIterable;

/**
 * These are some static DB requests on generic DAO type. Most of the time, the names are
 * quite easy to understand. There are some common rules: <li>If a method return a simple
 * object then the return value can be null (If the object is not found.)</li> <li>If a
 * method return a collection then the collection is always != null (but can be empty)</li>
 */
public final class DBRequests {

    public enum Comparator {
        EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL
    }

    /**
     * Disactivating the default ctor.
     */
    private DBRequests() {
        // disactivated
    }

    public static PageIterable<DaoDemand> getDemands(DemandState dState,
                                                     Date creationDate,
                                                     Comparator creationDateCmp,
                                                     DaoMember member,
                                                     DaoGroup asGroup,
                                                     Boolean hasAsGroup,
                                                     Integer popularity,
                                                     Comparator popularityCmp,
                                                     Boolean hasSelectedOffer,
                                                     Integer nbOffer,
                                                     Comparator offreCmp,
                                                     BigDecimal contribution,
                                                     Comparator contributionCmp,
                                                     Integer nbComments,
                                                     Comparator nbCommentsCmp,
                                                     Date validationDate,
                                                     Comparator validationDateCmp) {

        final Session session = SessionManager.getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(DaoDemand.class);

        if (dState != null) {
            criteria.add(Restrictions.eq("demandState", dState));
        }

        if (creationDate != null) {
            criteria.add(createNbCriterion(creationDateCmp, "creationDate", creationDate));
        }

        if (member != null) {
            criteria.add(Restrictions.eq("member", member));
        }

        if (asGroup != null) {
            criteria.add(Restrictions.eq("asGroup", asGroup));
        }

        if (hasAsGroup != null) {
            if (hasSelectedOffer) {
                criteria.add(Restrictions.isNotNull("asGroup"));
            } else {
                criteria.add(Restrictions.isNull("asGroup"));
            }
        }

        if (popularity != null) {
            criteria.add(createNbCriterion(popularityCmp, "popularity", popularity));
        }

        if (hasSelectedOffer != null) {
            if (hasSelectedOffer) {
                criteria.add(Restrictions.isNotNull("selectedOffer"));
            } else {
                criteria.add(Restrictions.isNull("selectedOffer"));
            }
        }

        if (nbOffer != null) {
            criteria.add(createSizeCriterion(offreCmp, "offers", nbOffer));
        }

        if (contribution != null) {
            criteria.add(createNbCriterion(contributionCmp, "contribution", contribution));
        }

        if (nbComments != null) {
            criteria.add(createSizeCriterion(nbCommentsCmp, "comments", nbComments));
        }

        if (validationDate != null) {
            criteria.add(createNbCriterion(validationDateCmp, "validationDate", validationDate));
        }

        return new CriteriaCollection<DaoDemand>(criteria);
    }

    private static Criterion createSizeCriterion(Comparator cmp, String element, int nb) {
        switch (cmp) {
        case EQUAL:
            return Restrictions.sizeEq(element, nb);
        case LESS:
            return Restrictions.sizeLt(element, nb);
        case LESS_EQUAL:
            return Restrictions.sizeLe(element, nb);
        case GREATER:
            return Restrictions.sizeGt(element, nb);
        case GREATER_EQUAL:
            return Restrictions.sizeGe(element, nb);
        default:
            return Restrictions.sizeEq(element, nb);
        }
    }

    private static Criterion createNbCriterion(Comparator cmp, String element, Object nb) {
        switch (cmp) {
        case EQUAL:
            return Restrictions.eq(element, nb);
        case LESS:
            return Restrictions.lt(element, nb);
        case LESS_EQUAL:
            return Restrictions.le(element, nb);
        case GREATER:
            return Restrictions.gt(element, nb);
        case GREATER_EQUAL:
            return Restrictions.ge(element, nb);
        default:
            return Restrictions.eq(element, nb);
        }
    }

    /**
     * Make sure you test if the return is != null:
     *
     * <pre>
     * public static Group create() {
     *     DaoGroup dao = DBRequests.getById(DaoGroup.class, 12);
     *     if (dao == null) {
     *         return null;
     *     }
     *     return new Group(dao);
     * }
     * </pre>
     *
     * @param <T>
     * @param persistant
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getById(final Class<T> persistant, final Integer id) {
        return (T) SessionManager.getSessionFactory().getCurrentSession().get(persistant, id);
    }

    public static <T> PageIterable<T> getAll(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>("from " + meta.getEntityName());
    }

    public static <T extends DaoUserContent> PageIterable<T> getAllUserContentOrderByDate(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return new QueryCollection<T>(SessionManager.createQuery("from " + meta.getEntityName() + " order by creationDate DESC"),
                SessionManager.createQuery("select count(*) from " + meta.getEntityName()));
    }

    public static <T> int count(final Class<T> persistent) {
        final ClassMetadata meta = SessionManager.getSessionFactory().getClassMetadata(persistent);
        return ((Long) SessionManager.getSessionFactory().getCurrentSession().createQuery("select count(*) from " + meta.getEntityName())
                .uniqueResult()).intValue();
    }

    public static PageIterable<DaoDemand> demandsOrderByPopularity() {
        return demandsOrderBy("popularity");
    }

    public static PageIterable<DaoDemand> demandsOrderByContribution() {
        return demandsOrderBy("contribution");
    }

    public static PageIterable<DaoDemand> demandsOrderByDate() {
        return demandsOrderBy("creationDate");
    }

    private static PageIterable<DaoDemand> demandsOrderBy(final String field) {
        return new QueryCollection<DaoDemand>("from DaoDemand where state == PENDING order by " + field);
    }

    // By kudosable
    // by near end
    // by date
    // by contrib
}
