package com.bloatit.model.admin;

import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.data.queries.DaoUserContentQuery;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.UserContent;
import com.bloatit.model.UserContentInterface;

public class UserContentAdminListFactory<T extends DaoUserContent, U extends UserContentInterface> extends IdentifiableAdminListFactory<T, U> {

    public static class DefaultFactory extends UserContentAdminListFactory<DaoUserContent, UserContent<DaoUserContent>> {
        // Just a rename
    }

    public UserContentAdminListFactory() {
        super(new DaoUserContentQuery<T>());
    }

    public UserContentAdminListFactory(final DaoUserContentQuery<T> factory) {
        super(factory);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected DaoUserContentQuery<T> getfactory() {
        return (DaoUserContentQuery) super.getfactory();
    }

    public void idEquals(final Integer id) {
        getfactory().idEquals(id);
    }

    public void teamByMember() {
        getfactory().groupByMember();
    }

    public void teamByAsTeam() {
        getfactory().groupByAsTeam();
    }

    public void orderByMember(final OrderType order) {
        getfactory().orderByMember(order);
    }

    public void orderByAsTeam(final OrderType order) {
        getfactory().orderByAsTeam(order);
    }

    public void orderByCreationDate(final OrderType orderType) {
        getfactory().orderByCreationDate(orderType);
    }

    public void deletedOnly() {
        getfactory().deletedOnly();
    }

    public void nonDeletedOnly() {
        getfactory().nonDeletedOnly();
    }

    public void withoutFile() {
        getfactory().withoutFile();
    }

    public void withFile() {
        getfactory().withFile();
    }

    public void withAnyTeam() {
        getfactory().withAnyTeam();
    }

    public void withNoTeam() {
        getfactory().withNoTeam();
    }

    public void fromMember(final Member member) {
        getfactory().fromMember(member.getDao());
    }

    public void fromTeam(final Team team) {
        getfactory().fromTeam(team.getDao());
    }
}
