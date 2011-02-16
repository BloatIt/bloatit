package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Iterator;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.framework.webserver.components.meta.HtmlText;
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
    public final HtmlNode getHeader(int column) {
        switch (column) {
        case 0:
            return new HtmlCheckbox("id_all", LabelPosition.BEFORE);
        case 1:
            return new HtmlText(tr("Author"));
        case 2:
            return new HtmlText(tr("asGroup"));
        case 3:
            return new HtmlText(tr("Creation date"));
        case 4:
            return new HtmlText(tr("Nb files"));
        case 5:
            return new HtmlText(tr("Deleted"));
        case 6:
            return new HtmlText(tr("Type"));
        default:
            // Do not use getColumnCount. It could be redefined.
            return getSubHeader(column - NB_COLUMNS);
        }
    }

    public HtmlNode getSubHeader(int column) {
        throw new NotImplementedException();
    }

    @Override
    public HtmlNode getBody(int column) {
        switch (column) {
        case 0:
            HtmlCheckbox htmlCheckbox = new HtmlCheckbox("id", LabelPosition.BEFORE);
            htmlCheckbox.addAttribute("value", getContent().getId().toString());
            return htmlCheckbox;
        case 1:
            return new HtmlText(getContent().getAuthor());
        case 2:
            return new HtmlText(getContent().getAsGroup());
        case 3:
            return new HtmlText(Context.getLocalizator().getDate(getContent().getCreationDate()).toString(FormatStyle.MEDIUM));
        case 4:
            return new HtmlText(String.valueOf(getContent().getFilesNumber()));
        case 5:
            return new HtmlText(getContent().isDeleted().toString());
        case 6:
            return new HtmlText(getContent().getType());
        default:
            // Do not use getColumnCount. It could be redefined.
            return getSubBody(column - NB_COLUMNS);
        }
    }

    public HtmlNode getSubBody(int column) {
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