package com.bloatit.model;

import java.util.Date;
import java.util.EnumSet;

import com.bloatit.data.DaoExternalServiceMembership;
import com.bloatit.data.DaoExternalServiceMembership.RightLevel;
import com.bloatit.data.DaoMember;
import com.bloatit.model.visitor.ModelClassVisitor;

public class ExternalServiceMembership extends Identifiable<DaoExternalServiceMembership> {

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoExternalServiceMembership, ExternalServiceMembership> {
        @SuppressWarnings("synthetic-access")
        @Override
        public ExternalServiceMembership doCreate(final DaoExternalServiceMembership dao) {
            return new ExternalServiceMembership(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static ExternalServiceMembership create(final DaoExternalServiceMembership dao) {
        return new MyCreator().create(dao);
    }

    private ExternalServiceMembership(final DaoExternalServiceMembership dao) {
        super(dao);
    }

    public final void authorize(String accessToken, String refreshToken, Date expirationDate) {
        getDao().authorize(accessToken, refreshToken, expirationDate);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final boolean isValid() {
        return getDao().isValid();
    }

    public final ExternalService getService() {
        return ExternalService.create(getDao().getService());
    }

    public final boolean isAuthorized() {
        return getDao().isAuthorized();
    }

    public final Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    public final DaoMember getMember() {
        return getDao().getMember();
    }

    public final EnumSet<RightLevel> getLevels() {
        return getDao().getLevels();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
