/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.Iterator;

import com.bloatit.framework.exceptions.general.ShallNotPassException;
import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Bug;
import com.bloatit.model.Feature;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ReportBugPageUrl;

public class FeatureBugListComponent extends HtmlDiv {

    public FeatureBugListComponent(final Feature feature) {
        super("padding");

        try {
            HtmlDiv reportBugBlock = new HtmlDiv("float_right");
            HtmlLink reportBugLink = new ReportBugPageUrl(feature.getSelectedOffer()).getHtmlLink(tr("Report a new bug"));
            reportBugLink.setCssClass("button");
            reportBugBlock.add(reportBugLink);
            add(reportBugBlock);
        } catch (UnauthorizedOperationException e) {
            Context.getSession().notifyError(Context.tr("An error prevented us from displaying selected offer. Please notify us."));
            throw new ShallNotPassException("User cannot access selected offer", e); 
        }

        // Open bugs
        final PageIterable<Bug> openBugs = feature.getOpenBugs();
        final HtmlTitle openBugsTitle = new HtmlTitle(Context.tr("Open bugs ({0})", openBugs.size()), 1);
        add(openBugsTitle);

        if (openBugs.size() == 0) {
            add(new HtmlParagraph(tr("No open bug.")));
        } else {
            final HtmlTable openBugsTable = new HtmlTable(new HtmlBugsTableModel(openBugs));
            add(openBugsTable);
        }

        // Closed bugs
        final PageIterable<Bug> closedBugs = feature.getClosedBugs();
        final HtmlTitle closedBugsTitle = new HtmlTitle(Context.tr("Closed bugs ({0})", closedBugs.size()), 1);
        add(closedBugsTitle);
        if (closedBugs.size() == 0) {
            add(new HtmlParagraph(tr("No closed bug.")));
        } else {
            final HtmlTable closedBugsTable = new HtmlTable(new HtmlBugsTableModel(closedBugs));
            add(closedBugsTable);
        }

    }

    private class HtmlBugsTableModel extends HtmlTableModel {

        private final Iterator<Bug> iterator;
        private Bug bug;

        public HtmlBugsTableModel(final PageIterable<Bug> openBugs) {
            iterator = openBugs.iterator();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public XmlNode getHeader(final int column) {
            HtmlText text = null;
            switch (column) {
                case 0:
                    text = new HtmlText(Context.tr("State"));
                    break;
                case 1:
                    text = new HtmlText(Context.tr("Level"));
                    break;
                case 2:
                    text = new HtmlText(Context.tr("Lot"));
                    break;
                case 3:
                    text = new HtmlText(Context.tr("Title"));
                    break;
                case 4:
                    text = new HtmlText(Context.tr("Last update"));
                    break;
                default:

                    break;
            }
            return text;
        }

        @Override
        public XmlNode getBody(final int column) {
            XmlNode text = null;
            switch (column) {
                case 0:
                    text = new HtmlText(bug.getState().toString());
                    break;
                case 1:
                    text = new HtmlText(bug.getErrorLevel().toString());
                    break;
                case 2:
                    text = new HtmlText(String.valueOf(bug.getMilestone().getPosition()));
                    break;
                case 3:
                    text = new BugPageUrl(bug).getHtmlLink(bug.getTitle());
                    break;

                case 4:
                    text = new HtmlText(Context.getLocalizator().getDate(bug.getLastUpdateDate()).toString());
                    break;
                default:

                    break;
            }
            return text;
        }

        @Override
        public boolean next() {
            if (iterator.hasNext()) {
                bug = iterator.next();
                return true;
            }
            return false;
        }

    }

}
