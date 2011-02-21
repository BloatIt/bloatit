package com.bloatit.model.admin;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.data.queries.DaoUserContentListFactory;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.model.Member;

public class UserContentAdminListFactory<T extends DaoUserContent, U extends UserContentAdmin<T>> {
    private final DaoUserContentListFactory<T> factory;

    public static class DefaultFactory extends UserContentAdminListFactory<DaoUserContent, UserContentAdmin<DaoUserContent>> {
        // Just a rename
    }

    public UserContentAdminListFactory() {
        factory = new DaoUserContentListFactory<T>();
    }

    public UserContentAdminListFactory(final DaoUserContentListFactory<T> factory) {
        this.factory = factory;
    }

    public void idEquals(final Integer id) {
        factory.idEquals(id);
    }

    public void groupByMember() {
        factory.groupByMember();
    }

    public void groupByAsGroup() {
        factory.groupByAsGroup();
    }

    public void orderByMember(final OrderType order) {
        factory.orderByMember(order);
    }

    public void orderByAsGroup(final OrderType order) {
        factory.orderByAsGroup(order);
    }

    public void orderByCreationDate(final OrderType orderType) {
        factory.orderByCreationDate(orderType);
    }

    public void orderBy(final String column, final OrderType orderType) {
        factory.orderBy(column, orderType);
    }

    public void deletedOnly() {
        factory.deletedOnly();
    }

    public void nonDeletedOnly() {
        factory.nonDeletedOnly();
    }

    public void withoutFile() {
        factory.withoutFile();
    }

    public void withFile() {
        factory.withFile();
    }

    public void withAnyGroup() {
        factory.withAnyGroup();
    }

    public void withNoGroup() {
        factory.withNoGroup();
    }

    public void fromMember(final Member member) {
        factory.fromMember(member.getDao());
    }

    public void fromGroup(final Group group) {
        factory.fromGroup(group.getDao());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PageIterable<U> list() {
        return (PageIterable) new AdminList.UserContentAdminList((PageIterable<DaoUserContent>) factory.createCollection());
    }

    protected DaoUserContentListFactory<T> getfactory() {
        return factory;
    }
}
