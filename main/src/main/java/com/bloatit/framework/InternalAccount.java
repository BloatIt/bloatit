package com.bloatit.framework;

import java.math.BigDecimal;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.InternalAccountRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoInternalAccount;

/**
 * An internal account is an account containing the money we store for a user. There is
 * one internal account for each user and only one. An internal account can never have an
 * amount under zero. An internal account can have some money blocked. When you contribute
 * on an idea, you do not spend the money directly, but it is blocked and you cannot use
 * it elsewhere.
 * 
 * @author tguyard
 */
public final class InternalAccount extends Account {

    private final DaoInternalAccount dao;

    protected InternalAccount(final DaoInternalAccount dao) {
        super();
        this.dao = dao;
    }

    public DaoInternalAccount getDao() {
        return dao;
    }

    /**
     * @return true if you can access the <code>Blocked</code> property.
     * @see #authenticate(AuthToken)
     */
    public boolean canAccessBlocked() {
        return new InternalAccountRight.Blocked().canAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
    }

    /**
     * Return the amount blocked into contribution on non finished idea.
     * 
     * @return a positive {@link BigDecimal}.
     * @throws UnauthorizedOperationException if you do not have the right to access the
     *         <code>Bloked</code> property.
     */
    public BigDecimal getBlocked() throws UnauthorizedOperationException {
        new InternalAccountRight.Blocked().tryAccess(calculateRole(getActorUnprotected().getLoginUnprotected()), Action.READ);
        return dao.getBlocked();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return dao;
    }

}
