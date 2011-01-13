package com.bloatit.framework;

import java.util.Date;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.right.ActorRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoActor;

/**
 * @see DaoActor
 */
public abstract class Actor extends Identifiable {

    protected abstract DaoActor getDaoActor();

    /**
     * @see Identifiable#getId()
     */
    @Override
    public final int getId() {
        return getDaoActor().getId();
    }

    /**
     * Tells if a user can access the <code>Email</code> property. You have to unlock this
     * Actor using the {@link Actor#authenticate(AuthToken)} method.
     *
     * @param action can be read/write/delete. for example use <code>READ</code> to know
     *        if you can use {@link Member#getGroups()}.
     * @return true if you can access the parameter <code>Email</code>.
     * @see Actor#getEmail()
     * @see Actor#setEmail()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessEmail(Action action) {
        return new ActorRight.Email().canAccess(calculateRole(getLoginUnprotected()), action);
    }

    /**
     * @see DaoActor#getEmail()
     * @throws UnauthorizedOperationException if you don't have the <code>READ</code>
     *         right on the <code>Email</code> property
     */
    public final String getEmail() throws UnauthorizedOperationException {
        new ActorRight.Email().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getDaoActor().getEmail();
    }

    /**
     * @see DaoActor#setEmail()
     * @throws UnauthorizedOperationException if you don't have the <code>WRITE</code>
     *         right on the <code>Email</code> property
     */
    public final void setEmail(final String email) throws UnauthorizedOperationException {
        new ActorRight.Email().tryAccess(calculateRole(getLoginUnprotected()), Action.WRITE);
        getDaoActor().setEmail(email);
    }

    protected final String getLoginUnprotected() {
        return getDaoActor().getLogin();
    }

    /**
     * @return true if you have the <code>READ</code> right on the Login property.
     * @see Actor#getEmail()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canAccessLogin() {
        return new ActorRight.Login().canAccess(Action.READ);
    }

    /**
     * @see DaoActor#getLogin()
     * @throws UnauthorizedOperationException if you don't have the <code>READ</code>
     *         right on the <code>Login</code> property
     */
    public final String getLogin() throws UnauthorizedOperationException {
        new ActorRight.Login().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getLoginUnprotected();
    }

    /**
     * @return true if you can access the DateCreation property.
     * @see Actor#getDateCreation()
     */
    public final boolean canAccessDateCreation() {
        return new ActorRight.DateCreation().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @see DaoActor#getDateCreation()
     * @throws UnauthorizedOperationException if you don't have the right to access the
     *         DateCreation property.
     */
    public final Date getDateCreation() throws UnauthorizedOperationException {
        new ActorRight.DateCreation().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return getDaoActor().getDateCreation();
    }

    /**
     * @return true if you can access the <code>InternalAccount</code> property.
     * @see Actor#getInternalAccount()
     * @see Actor#authenticate(AuthToken)
     */
    public final boolean canGetInternalAccount() {
        return new ActorRight.InternalAccount().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * The internal account is the account we manage internally. Users can add/get money
     * to/from it, and can use this money to contribute on projects.
     *
     * @throw UnauthorizedOperationException if you do not have the right to access the
     *        <code>InternalAccount</code> property.
     */
    public final InternalAccount getInternalAccount() throws UnauthorizedOperationException {
        new ActorRight.InternalAccount().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return new InternalAccount(getDaoActor().getInternalAccount());
    }

    /**
     * @return true if you can access the <code>ExternalAccount</code> property.
     */
    public final boolean canGetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getLoginUnprotected()), Action.READ);
    }

    /**
     * @throws UnauthorizedOperationException if you haven't the right to access the <code>ExtenralAccount</code> property.
     */
    public final ExternalAccount getExternalAccount() throws UnauthorizedOperationException {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getLoginUnprotected()), Action.READ);
        return new ExternalAccount(getDaoActor().getExternalAccount());
    }

    protected DaoActor getDao() {
        return getDaoActor();
    }
}
