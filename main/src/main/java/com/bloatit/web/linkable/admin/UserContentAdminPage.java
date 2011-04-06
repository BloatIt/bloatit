package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.EnumSet;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webserver.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webserver.components.form.Displayable;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.model.Team;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/usercontent")
public abstract class UserContentAdminPage<U extends DaoUserContent, V extends UserContentInterface<U>, T extends UserContentAdminListFactory<U, V>>
        extends IdentifiablesAdminPage<U, V, UserContentAdminListFactory<U, V>> {

    public enum OrderByUserContent implements Displayable {
        NOTHING(tr("No order")), //
        MEMBER(tr("Member")), //
        TEAM(tr("Team")), //
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
    private final DisplayableFilterType filterTeam;

    private final T factory;
    private final UserContentAdminPageUrl url;

    protected UserContentAdminPage(final UserContentAdminPageUrl url, final T factory) {
        super(url, factory);
        this.url = url;
        this.factory = factory;
        filterDeleted = url.getFilterDeleted();
        filterFile = url.getFilterFile();
        filterTeam = url.getFilterTeam();

        // Save parameters
        Context.getSession().addParameter(url.getOrderByStrParameter());
        Context.getSession().addParameter(url.getAscParameter());
        Context.getSession().addParameter(url.getFilterDeletedParameter());
        Context.getSession().addParameter(url.getFilterFileParameter());
        Context.getSession().addParameter(url.getFilterTeamParameter());

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
        if (filterTeam == DisplayableFilterType.WITH) {
            factory.withAnyTeam();
        } else if (filterTeam == DisplayableFilterType.WITHOUT) {
            factory.withNoTeam();
        }
    }

    protected void addAsTeamFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupAsTeamData = url.getFilterTeamParameter().pickFieldData();
        final HtmlDropDown groupAsTeam = new HtmlDropDown(groupAsTeamData.getName());
        groupAsTeam.setDefaultValue(groupAsTeamData.getSuggestedValue());
        groupAsTeam.addErrorMessages(groupAsTeamData.getErrorMessages());
        groupAsTeam.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupAsTeam.setLabel(tr("Filter by Content created as a group"));
        filterForm.add(groupAsTeam);
    }

    protected void addHasFileFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupFileData = url.getFilterFileParameter().pickFieldData();
        final HtmlDropDown groupFile = new HtmlDropDown(groupFileData.getName());
        groupFile.setDefaultValue(groupFileData.getSuggestedValue());
        groupFile.addErrorMessages(groupFileData.getErrorMessages());
        groupFile.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupFile.setLabel(tr("Filter by Content with file"));
        filterForm.add(groupFile);
    }

    protected void addIsDeletedFilter(final HtmlForm filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupDeletedData = url.getFilterDeletedParameter().pickFieldData();
        final HtmlDropDown groupDeleted = new HtmlDropDown(groupDeletedData.getName());
        groupDeleted.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupDeleted.setDefaultValue(groupDeletedData.getSuggestedValue());
        groupDeleted.addErrorMessages(groupDeletedData.getErrorMessages());
        groupDeleted.setLabel(tr("Filter by deleted content"));
        filterForm.add(groupDeleted);
    }

    @Override
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
                } catch (final UnauthorizedOperationException e) {
                    session.notifyBad("HAHAHA !");
                    throw new ShallNotPassException("UnauthorizedOperationException on admin page", e);
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

    protected void addAsTeamColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("asTeam");
        tableModel.addColumn(clonedUrl.getHtmlLink(tr("asTeam")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                try {
                    final Team asTeam = element.getAsTeam();
                    if (asTeam != null) {
                        return asTeam.getLogin();
                    }
                    return "null";
                } catch (final UnauthorizedOperationException e) {
                    throw new ShallNotPassException("UnauthorizedOperationException on admin page", e);
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
                } catch (final UnauthorizedOperationException e) {
                    throw new ShallNotPassException("UnauthorizedOperationException on admin page", e);
                }
            }
        });
        return clonedUrl;
    }

    @Override
    protected void addActions(final HtmlDropDown dropDown, final HtmlBranch actionTeam) {
        // redefine me in subclasses.
        dropDown.addDropDownElements(new AdminActionManager().userContentActions());
    }

    @Override
    protected abstract void addColumns(HtmlGenericTableModel<V> tableModel);

    @Override
    protected abstract void addFormFilters(HtmlForm form);

    @Override
    protected T getFactory() {
        return factory;
    }

}
