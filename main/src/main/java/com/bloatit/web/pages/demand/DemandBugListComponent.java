/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages.demand;

import java.util.Iterator;

import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.advanced.HtmlTable;
import com.bloatit.framework.webserver.components.advanced.HtmlTable.HtmlTableModel;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.model.Bug;
import com.bloatit.model.Demand;

public class DemandBugListComponent extends HtmlDiv {

    private final Demand demand;

    public DemandBugListComponent(final Demand demand) {
        super();
        this.demand = demand;


        PageIterable<Bug> openBugs = demand.getOpenBugs();


        HtmlTitle openBugsTitle = new HtmlTitle(Context.tr("Open bugs ({0})", openBugs.size()), 1);
        add(openBugsTitle);

        HtmlTable openBugsTable = new HtmlTable(new HtmlBugsTableModel(openBugs));
        add(openBugsTable);
    }


    private class HtmlBugsTableModel extends HtmlTableModel {

        private final Iterator<Bug> iterator;
        private Bug bug;

        public HtmlBugsTableModel(PageIterable<Bug> openBugs) {
            iterator = openBugs.iterator();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public XmlNode getHeader(int column) {
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
        public XmlNode getBody(int column) {
            HtmlText text = null;
            switch (column) {
            case 0:
                text = new HtmlText(bug.getState().toString());
                break;
            case 1:
                text = new HtmlText(bug.getErrorLevel().toString());
                break;
            case 2:
                //TODO find batch count
                text = new HtmlText(String.valueOf(2));
                break;
            case 3:
                text = new HtmlText(bug.getTitle());
                break;

            case 4:
                text = new HtmlText(Context.getLocalizator().getDate(bug.getLastUpdateDate()).toString(FormatStyle.SHORT));
                break;
            default:

                break;
            }
            return text;
        }

        @Override
        public boolean next() {
            if(iterator.hasNext()) {
                bug = iterator.next();
                return true;
            }
            return false;
        }

    }

}
