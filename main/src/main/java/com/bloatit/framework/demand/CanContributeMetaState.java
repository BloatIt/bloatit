package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoOffer;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public abstract class CanContributeMetaState extends AbstractDemandState {

    public CanContributeMetaState(Demand demand) {
        super(demand);
    }

    protected abstract AbstractDemandState notifyAddContribution();

    @Override
    public final AbstractDemandState eventAddContribution(Member member, BigDecimal amount, String comment) throws NotEnoughMoneyException,
            UnauthorizedOperationException {
        demand.getDao().addContribution(member.getDao(), amount, comment);
        return notifyAddContribution();
    }

    protected final AbstractDemandState handleEvent() {
        BigDecimal contribution = demand.getDao().getContribution();
        DaoOffer selectedOffer = demand.getDao().getSelectedOffer();
        if (selectedOffer != null && contribution.compareTo(selectedOffer.getAmount()) >= 0) {
            return new DeveloppingState(demand);
        }
        return this;
    }

}
