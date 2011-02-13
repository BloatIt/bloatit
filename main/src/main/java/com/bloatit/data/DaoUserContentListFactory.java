package com.bloatit.data;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;


class DaoUserContentListFactory<T extends DaoUserContent> extends DaoIdentifiableListFactory<T> {

    private static final String MEMBER = "member";
    private static final String FILES = "files";
    private static final String IS_DELETED = "isDeleted";
    private static final String AS_GROUP = "asGroup";

    protected DaoUserContentListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoUserContentListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoUserContent.class));
    }

    public void groupByMember() {
        add(Projections.groupProperty(MEMBER));
    }

    public void groupByAsGroup(Order order) {
        add(Projections.groupProperty(AS_GROUP));
    }

    public void orderByMember(DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(MEMBER));
        } else {
            addOrder(Order.desc(MEMBER));
        }
    }

    public void orderByAsGroup(DaoAbstractListFactory.OrderType order) {
        if (order == OrderType.ASC) {
            addOrder(Order.asc(AS_GROUP));
        } else {
            addOrder(Order.desc(AS_GROUP));
        }
    }

    public void onlyDeleted() {
        add(Restrictions.eq(IS_DELETED, false));
    }

    public void onlyNonDeleted() {
        add(Restrictions.eq(IS_DELETED, true));
    }

    public void withoutFile() {
        add(Restrictions.isEmpty(FILES));
    }

    public void withFile() {
        add(Restrictions.isNotEmpty(FILES));
    }

    public void withAnyGroup() {
        add(Restrictions.isNotNull(AS_GROUP));
    }

    public void withNoGroup() {
        add(Restrictions.isNull(AS_GROUP));
    }

    public void fromMember(DaoMember member) {
        add(Restrictions.eq(MEMBER, member));
    }

    public void fromGroup(DaoGroup group) {
        add(Restrictions.eq(AS_GROUP, group));
    }
}