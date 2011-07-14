package com.bloatit.model;

import java.util.Date;
import java.util.EnumSet;

import com.bloatit.data.DaoExternalService;
import com.bloatit.data.DaoExternalService.RightLevel;

public class ExternalService extends Identifiable<DaoExternalService> {

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoExternalService, ExternalService> {
        @SuppressWarnings("synthetic-access")
        @Override
        public ExternalService doCreate(final DaoExternalService dao) {
            return new ExternalService(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static ExternalService create(final DaoExternalService dao) {
        return new MyCreator().create(dao);
    }

    private ExternalService(final DaoExternalService dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final boolean isValid() {
        return getDao().isValid();
    }

    public final String getName() {
        return getDao().getName();
    }

    public final String getToken() {
        return getDao().getToken();
    }

    public final boolean isAuthorized() {
        return getDao().isAuthorized();
    }

    public final String getRefreshToken() {
        return getDao().getRefreshToken();
    }

    public final Date getExpirationDate() {
        return getDao().getExpirationDate();
    }

    public final Member getMember() {
        return Member.create(getDao().getMember());
    }

    public final EnumSet<RightLevel> getLevels() {
        return getDao().getLevels();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public final void authorize(final String accessToken, final String refreshToken, final Date expirationDate) {
        getDao().authorize(accessToken, refreshToken, expirationDate);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
