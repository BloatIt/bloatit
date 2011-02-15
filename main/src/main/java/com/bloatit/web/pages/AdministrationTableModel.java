package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.model.UserContentAdministration;
import com.bloatit.model.UserContentAdministrationListFactory;

class AdministrationTableModel<T extends DaoUserContent> extends HtmlTableModel {

    private static final int NB_COLUMNS = 6;
    private Iterator<UserContentAdministration<T>> iterator;
    private UserContentAdministration<T> content;
    private final UserContentAdministrationListFactory<T> factory;

    public AdministrationTableModel(UserContentAdministrationListFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public final String getHeader(int column) {
        switch (column) {
        case 0:
            return tr("Creation date");
        case 1:
            return tr("Type");
        case 2:
            return tr("Deleted");
        case 3:
            return tr("File number");
        case 4:
            return tr("Author");
        case 5:
            return tr("asGroup");
        default:
            // Do not use getColumnCount. It could be redefined.
            return getSubHeader(column - NB_COLUMNS);
        }
    }

    public String getSubHeader(int column) {
        throw new NotImplementedException();
    }

    @Override
    public String getBody(int column) {
        switch (column) {
        case 0:
            return getContent().getCreationDate().toString();
        case 1:
            return getContent().getType();
        case 2:
            return getContent().isDeleted().toString();
        case 3:
            return String.valueOf(getContent().getFilesNumber());
        case 4:
            return getContent().getAuthor();
        case 5:
            return getContent().getAsGroup();
        default:
            // Do not use getColumnCount. It could be redefined.
            return getSubBody(column - NB_COLUMNS);
        }
    }

    public String getSubBody(int column) {
        throw new NotImplementedException();
    }

    @Override
    public boolean next() {
        if (iterator == null) {
            iterator = factory.ListUserContents().iterator();
        }
        if (iterator.hasNext()) {
            setContent(iterator.next());
            return true;
        }
        return false;
    }

    private final void setContent(UserContentAdministration<T> content) {
        this.content = content;
    }

    protected UserContentAdministration<T> getContent() {
        return content;
    }

    public UserContentAdministrationListFactory<T> getFactory() {
        return factory;
    }

}