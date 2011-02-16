package com.bloatit.model.admin;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.data.queries.DaoUserContentListFactory;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Group;
import com.bloatit.model.Member;

public class UserContentAdministrationListFactory<T extends DaoUserContent> {
    private DaoUserContentListFactory<T> factory;

    public UserContentAdministrationListFactory() {
        factory = new DaoUserContentListFactory<T>();
    }

    public void idEquals(Integer id) {
        factory.idEquals(id);
    }

    public void groupByMember() {
        factory.groupByMember();
    }

    public void groupByAsGroup() {
        factory.groupByAsGroup();
    }

    public void orderByMember(OrderType order) {
        factory.orderByMember(order);
    }

    public void orderByAsGroup(OrderType order) {
        factory.orderByAsGroup(order);
    }
    
    public void orderByCreationDate(OrderType orderType) {
        factory.orderByCreationDate(orderType);
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

    public void fromMember(Member member) {
        factory.fromMember(member.getDao());
    }

    public void fromGroup(Group group) {
        factory.fromGroup(group.getDao());
    }

    public PageIterable<UserContentAdministration<T>> ListUserContents() {
        return new UserContentAdministrationList<T>(factory.createCollection());
    }


}