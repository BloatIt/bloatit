package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.data.DaoAccount;
import com.bloatit.data.DaoInternalAccount;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.right.InternalAccountRight;
import com.bloatit.model.right.RightManager.Action;

/**
 * An internal account is an account containing the money we store for a user. There is
 * one internal account for each user and only one. An internal account can never have an
 * amount under zero. An internal account can have some money blocked. When you contribute
 * on an idea, you do not spend the money directly, but it is blocked and you cannot use
 * it elsewhere.
 *
 * @author tguyard
 */
public final class InternalAccount extends Account<DaoInternalAccount> {

    public static InternalAccount create(final DaoInternalAccount dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoInternalAccount> created = CacheManager.get(dao);
            if (created == null) {
                return new InternalAccount(dao);
            }
            return (InternalAccount) created;
        }
        return null;
    }

    private InternalAccount(final DaoInternalAccount dao) {
        super(dao);
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
        return getDao().getBlocked();
    }

    @Override
    protected DaoAccount getDaoAccount() {
        return getDao();
    }

}
