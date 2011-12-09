package com.bloatit.model;

import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoExternalService;
import com.bloatit.model.visitor.ModelClassVisitor;

public class ExternalService extends UserContent<DaoExternalService> {
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

    public ExternalService(final Member author, final Team asTeam, final Description description) {
        this(DaoExternalService.createAndPersist(author.getDao(), asTeam != null ? asTeam.getDao() : null, description.getDao()));
    }

    private ExternalService(final DaoExternalService dao) {
        super(dao);
    }

    public void setLogo(final FileMetadata fileImage) {
        getDao().setLogo(fileImage.getDao());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public FileMetadata getLogo() {
        return FileMetadata.create(getDao().getLogo());
    }

    public DaoDescription getDescription() {
        return getDao().getDescription();
    }

    public String getToken() {
        return getDao().getToken();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
