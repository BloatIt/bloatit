package com.bloatit.model.admin;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.model.Member;

public class KudosableAdmin<T extends DaoKudosable> extends UserContentAdmin<T> {

    protected KudosableAdmin(final T dao) {
        super(dao);
    }

    public static KudosableAdmin<DaoKudosable> create(final DaoKudosable dao) {
        if (dao != null) {
            return new KudosableAdmin<DaoKudosable>(dao);
        }
        return null;
    }

    public static KudosableAdmin<DaoKudosable> createKudosable(Integer id) {
        KudosableAdminListFactory<DaoKudosable, KudosableAdmin<DaoKudosable>> factory = new KudosableAdminListFactory<DaoKudosable, KudosableAdmin<DaoKudosable>>();
        factory.idEquals(id);
        if (factory.list().iterator().hasNext()){
            return factory.list().iterator().next();
        }
        return null;
    }

    public boolean isPopularityLocked() {
        return getDao().isPopularityLocked();
    }

    public final PopularityState getState() {
        return getDao().getState();
    }

    public final int getPopularity() {
        return getDao().getPopularity();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Modifiers
    // ////////////////////////////////////////////////////////////////////////

    public void lockPopularity() {
        getDao().lockPopularity();
    }

    public void unlockPopularity() {
        getDao().unlockPopularity();
    }

    public void setState(final PopularityState state) {
        getDao().setState(state);
    }

    public void addKudos(Member member, int value) {
        getDao().addKudos(member.getDao(), value);
    }
}
