//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.admin;

import java.util.EnumSet;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel;
import com.bloatit.framework.webprocessor.components.advanced.HtmlGenericTableModel.StringColumnGenerator;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Team;
import com.bloatit.model.UserContentInterface;
import com.bloatit.model.admin.UserContentAdminListFactory;
import com.bloatit.web.url.UserContentAdminPageUrl;

@ParamContainer("admin/usercontent")
public abstract class UserContentAdminPage<U extends DaoUserContent, V extends UserContentInterface, T extends UserContentAdminListFactory<U, V>>
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
            return Context.tr(displayName);
        }

        private OrderByUserContent(final String displayName) {
            this.displayName = displayName;
        }

        // Fake tr
        private static String tr(final String fake) {
            return fake;
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

    protected void addAsTeamFilter(final HtmlBranch filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupAsTeamData = url.getFilterTeamParameter().pickFieldData();
        final HtmlDropDown groupAsTeam = new HtmlDropDown(groupAsTeamData.getName());
        groupAsTeam.addErrorMessages(groupAsTeamData.getErrorMessages());
        groupAsTeam.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupAsTeam.setLabel(Context.tr("Filter by Content created as a group"));
        filterForm.add(groupAsTeam);
    }

    protected void addHasFileFilter(final HtmlBranch filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupFileData = url.getFilterFileParameter().pickFieldData();
        final HtmlDropDown groupFile = new HtmlDropDown(groupFileData.getName());
        groupFile.addErrorMessages(groupFileData.getErrorMessages());
        groupFile.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupFile.setLabel(Context.tr("Filter by Content with file"));
        filterForm.add(groupFile);
    }

    protected void addIsDeletedFilter(final HtmlBranch filterForm, final UserContentAdminPageUrl url) {
        final FieldData groupDeletedData = url.getFilterDeletedParameter().pickFieldData();
        final HtmlDropDown groupDeleted = new HtmlDropDown(groupDeletedData.getName());
        groupDeleted.addDropDownElements(EnumSet.allOf(DisplayableFilterType.class));
        groupDeleted.addErrorMessages(groupDeletedData.getErrorMessages());
        groupDeleted.setLabel(Context.tr("Filter by deleted content"));
        filterForm.add(groupDeleted);
    }

    @Override
    protected void addTypeColumn(final HtmlGenericTableModel<V> tableModel) {
        tableModel.addColumn(Context.tr("Type"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return element.getClass().getSimpleName();
            }
        });
    }

    protected void addIsDeletedColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("isDeleted");
        tableModel.addColumn(clonedUrl.getHtmlLink(Context.tr("Deleted")), new StringColumnGenerator<V>() {
            @SuppressWarnings("synthetic-access")
            @Override
            public String getStringBody(final V element) {
                // try {
                return String.valueOf(element.isDeleted());
                // FIXME: isDeleted no more nedd admin rigth. There was a reason
                // to request admin rigth ?
                // } catch (final UnauthorizedOperationException e) {
                // getSession().notifyWarning("HAHAHA !");
                // throw new
                // ShallNotPassException("UnauthorizedOperationException on admin page",
                // e);
                // }
            }
        });
    }

    protected void addNbFilesColumn(final HtmlGenericTableModel<V> tableModel) {
        tableModel.addColumn(Context.tr("Nb files"), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return String.valueOf(element.getFiles().size());
            }
        });
    }

    protected void addCreationDateColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("creationDate");
        tableModel.addColumn(clonedUrl.getHtmlLink(Context.tr("Creation date")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return Context.getLocalizator().getDate(element.getCreationDate()).toString(FormatStyle.MEDIUM);
            }
        });
    }

    protected void addAsTeamColumn(final HtmlGenericTableModel<V> tableModel, final UserContentAdminPageUrl clonedUrl) {
        clonedUrl.setOrderByStr("asTeam");
        tableModel.addColumn(clonedUrl.getHtmlLink(Context.tr("asTeam")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                final Team asTeam = element.getAsTeam();
                if (asTeam != null) {
                    return asTeam.getLogin();
                }
                return "null";
            }
        });
    }

    protected UserContentAdminPageUrl addAuthorColumn(final HtmlGenericTableModel<V> tableModel) {
        final UserContentAdminPageUrl clonedUrl = url.clone();
        clonedUrl.setOrderByStr("m.login");
        tableModel.addColumn(clonedUrl.getHtmlLink(Context.tr("Author")), new StringColumnGenerator<V>() {
            @Override
            public String getStringBody(final V element) {
                return element.getMember().getLogin();
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
    protected abstract void addFormFilters(HtmlBranch form);

    @Override
    protected T getFactory() {
        return factory;
    }

}
