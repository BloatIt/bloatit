package com.bloatit.model.admin;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoDemand;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoProject;

public class DemandAdmin extends KudosableAdmin<DaoDemand> {

    protected DemandAdmin(final DaoDemand dao) {
        super(dao);
    }

    public static DemandAdmin create(final DaoDemand dao) {
        if (dao != null) {
            return new DemandAdmin(dao);
        }
        return null;
    }

    public static DemandAdmin createDemand(Integer id) {
        DemandAdminListFactory factory = new DemandAdminListFactory();
        factory.idEquals(id);
        if (factory.list().iterator().hasNext()) {
            return factory.list().iterator().next();
        }
        return null;
    }

    public DaoDescription getDescription() {
        return getDao().getDescription();
    }

    public DemandState getDemandState() {
        return getDao().getDemandState();
    }

    public BigDecimal getContribution() {
        return getDao().getContribution();
    }

    public DaoProject getProject() {
        return getDao().getProject();
    }

    // ////////////////////////////////////////////////////////////////////////
    // Modifiers
    // ////////////////////////////////////////////////////////////////////////

    public void computeSelectedOffer() {
        getDao().computeSelectedOffer();
    }

    public void setDemandState(DemandState demandState) {
        getDao().setDemandState(demandState);
    }

}
