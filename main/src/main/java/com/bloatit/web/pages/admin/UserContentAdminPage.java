package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.common.Log;
import com.bloatit.data.DaoUserContent;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.ColumnGenerator;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.form.Displayable;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormBlock;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Group;
import com.bloatit.model.UserContent;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.admin.IdentifiableAdminListFactory;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.AdministrationActionUrl;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/usercontent")
public abstract class UserContentAdminPage<U extends DaoUserContent, V extends UserContentInterface<U>, T extends UserContentAdminListFactory<U, V>>
        extends IdentifiablesAdminPage<U, V, UserContentAdminListFactory<U,V>> {

    public enum OrderByUserContent implements Displayable {
        NOTHING(tr("No order")), //
        MEMBER(tr("Member")), //
        GROUP(tr("Group")), //
        DATE(tr("Creation date")), //
        TYPE(tr("Type"));

        private String displayName;

        @Override
        public String getDisplayName() {
            return displayName;
        }

        private OrderByUserContent(final String displayName) {
            this.displayName = displayName;
        }
    }

    @RequestParam
    @Optional("WITHOUT")
    private final DisplayableFilterType filterDeleted;

    @RequestParam
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterFile;

    @RequestParam
    @Optional("NO_FILTER")
    private final DisplayableFilterType filterGroup;

    private final T factory;
    private final UserContentAdminPageUrl url;

    protected UserContentAdminPage(final UserContentAdminPageUrl url, final T factory) {
        super(url, factory); 
        this.url = url;
        this.factory = factory;
        filterDeleted = url.getFilterDeleted();
        filterFile = url.getFilterFile();
        filterGroup = url.getFilterGroup();

        // Save parameters
        Context.getSession().addParameter(url.getOrderByStrParameter());
        Context.getSession().addParameter(url.getAscParameter());
        Context.getSession().addParameter(url.getFilterDeletedParameter());
        Context.getSession().addParameter(url.getFilterFileParameter());
        Context.getSession().addParameter(url.getFilterGroupParameter());
        
        if (filterDeleted == DisplayableFilterType.WITH) {
            factory.deletedOnly();
        } else if (filterDeleted == DisplayableFilterType.WITHOUT) {
            factory.nonDeletedOnly();
        }
        if (filterFile == DisplayableFilterType.WITH) {
            factory.withoutFile();
        } else if (filterFile == DisplayableFilterType.WITHOUT) {
            factory.withFile();
        }
        if (filterGroup == DisplayableFilterType.WITH) {
            factory.withAnyGroup();
        } else if (filterGroup == DisplayableFilterType.WITHOUT) {
            factory.withNoGroup();
        }
    }

    protected void addAsGroupFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final HtmlDropDown groupAsGroup = new HtmlDropDown(url.getFilterGroupParameter().fieldData());
        groupAsGroup.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupAsGroup.setLabel(tr("Filter by Content created as a group"));
        filterForm.add(groupAsGroup);
    }

    protected void addHasFileFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final HtmlDropDown groupFile = new HtmlDropDown(url.getFilterFileParameter().fieldData());
        groupFile.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupFile.setLabel(tr("Filter by Content with file"));
        filterForm.add(groupFile);
    }

    protected void addIsDeletedFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final HtmlDropDown groupDeleted = new HtmlDropDown(url.getFilterDeletedParameter().fieldData());
        groupDeleted.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupDeleted.setLabel(tr("Filter by deleted content"));
        filterForm.add(groupDeleted);
    }

    protected void addTypeColumn(final HtmlGenericTableModel<V> tableModel) {
        tableModel.addColumn(tr("Type"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return element.getClass().getSimpleName();
            }
        });
    }

    protected void addIsDeletedColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("isDeleted");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Deleted")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                try {
                    return String.valueOf(element.isDeleted());
                } catch (UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });
    }

    protected void addNbFilesColumn(final HtmlGenericTableModel<V> tableModel) {
        tableModel.addColumn(tr("Nb files"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return String.valueOf(element.getFiles().size());
            }
        });
    }

    protected void addCreationDateColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("creationDate");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Creation date")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return Context.getLocalizator().getDate(element.getCreationDate()).toString(FormatStyle.MEDIUM);
            }
        });
    }

    protected void addAsGroupColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("asGroup");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("asGroup")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                try {
                    Group asGroup = element.getAsGroup();
                    if (asGroup != null) {
                        return asGroup.getLogin();
                    }
                    return "null";
                } catch (UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });
    }

    protected UserContentAdminPageUrl addAuthorColumn(final HtmlGenericTableModel<V> tableModel) {
        final UserContentAdminPageUrl clonedUrl = url.clone();
        clonedUrl.setOrderByStr("m.login");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("Author")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                try {
                    return element.getAuthor().getLogin();
                } catch (UnauthorizedOperationException e) {
                    Log.web().fatal("", e);
                    return "";
                }
            }
        });
        return clonedUrl;
    }

    protected void addActions(final HtmlDropDown dropDown, final HtmlBranch actionGroup) {
        // redefine me in subclasses.
        dropDown.addDropDownElements(new AdminActionManager().userContentActions());
    }

    protected abstract void addColumns(HtmlGenericTableModel<V> tableModel);

    protected abstract void addFormFilters(HtmlForm form);

    protected T getFactory() {
        return factory;
    }

}
