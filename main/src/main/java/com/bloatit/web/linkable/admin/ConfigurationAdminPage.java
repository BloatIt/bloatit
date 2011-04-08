/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.admin;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.bloatit.common.ReloadableConfiguration;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.utils.TimeRenderer;
import com.bloatit.framework.utils.TimeRenderer.TimeBase;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.form.HtmlCheckbox;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.ConfigurationAdminActionUrl;
import com.bloatit.web.url.ConfigurationAdminPageUrl;

@ParamContainer("admin/configure")
public class ConfigurationAdminPage extends AdminPage {
    private final ConfigurationAdminPageUrl url;

    /**
     * @param url
     */
    public ConfigurationAdminPage(ConfigurationAdminPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateConfAdmin());

        // layout.addRight(new SideBarDocumentationBlock("markdown"));
        return layout;
    }

    private HtmlElement generateConfAdmin() {
        HtmlTitleBlock master = new HtmlTitleBlock("Administrate configurations", 1);
        HtmlForm form = new HtmlForm(new ConfigurationAdminActionUrl().urlString());
        master.add(form);
        form.add(new HtmlTable(new ConfigurationTableModel(ReloadableConfiguration.getConfigurations())));
        form.add(new HtmlSubmit(Context.tr("Submit")));

        return master;
    }

    @Override
    public void processErrors() throws RedirectException {

    }

    @Override
    protected String getPageTitle() {
        return null;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.pushLink(new AdminHomePageUrl().getHtmlLink("Admin"));
        breadcrumb.pushLink(url.getHtmlLink("Configuration"));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private class ConfigurationTableModel extends HtmlTableModel {
        private Iterator<ReloadableConfiguration> iterator;
        private Set<ReloadableConfiguration> configurations;
        private ReloadableConfiguration configuration;

        private static final int NAME = 0;
        private static final int DATE = 1;
        private static final int ACTION = 2;

        public ConfigurationTableModel(final Set<ReloadableConfiguration> configurations) {
            this.configurations = configurations;
            iterator = configurations.iterator();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public XmlNode getHeader(int column) {
            switch (column) {
                case NAME:
                    return new HtmlText(Context.tr("Configuration"));
                case DATE:
                    return new HtmlText(Context.tr("Last reload"));
                case ACTION:
                    return new HtmlText(Context.tr("Reload"));
                default:
                    return new HtmlText("");
            }
        }

        @Override
        public XmlNode getBody(int column) {
            switch (column) {
                case NAME:
                    return new HtmlText(configuration.getName());
                case DATE:
                    long millis = new Date().getTime() - configuration.getLastReload().getTime();
                    return new HtmlText(new TimeRenderer(millis).renderRange(TimeBase.DAY, FormatStyle.MEDIUM));
                case ACTION:
                    HtmlCheckbox box = new HtmlCheckbox("toReload", LabelPosition.AFTER);
                    box.addAttribute("value", configuration.getName());
                    return box;
                default:
                    return new HtmlText("");
            }
        }

        @Override
        public boolean next() {
            if (iterator == null) {
                iterator = configurations.iterator();
            }

            if (iterator.hasNext()) {
                configuration = iterator.next();
                return true;
            }
            return false;
        }
    }
}
