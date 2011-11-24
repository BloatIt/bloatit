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
package com.bloatit.framework.webprocessor.components.advanced;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlTable.HtmlLineTableModel.HtmlTableLine;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

/**
 * This class display an html table using a data model (HtmlTableModel) For the
 * header then for each line, the table object will call the next method then
 * use the different getter to get the informations for the current line.
 */
public class HtmlTable extends HtmlGenericElement {

    private final HtmlTableModel model;
    private int columnCount;

    public HtmlTable(final HtmlTableModel model) {
        super("table");
        this.model = model;

        generateHeader();
        generateBody();

    }

    private void generateBody() {
        final HtmlGenericElement tbody = new HtmlGenericElement("tbody");
        while (getModel().next()) {
            final HtmlGenericElement tr = new HtmlGenericElement("tr");
            columnCount = getModel().getColumnCount();

            if (getModel().getLineCss() != null) {
                tr.setCssClass(getModel().getLineCss());
            }

            if (getModel().getLineId() != null) {
                tr.setId(getModel().getLineId());
            }

            for (int i = 0; i < columnCount; i++) {
                final HtmlGenericElement td = new HtmlGenericElement("td");
                td.add(getModel().getBody(i));
                if (getModel().getColumnCss(i) != null) {
                    td.setCssClass(getModel().getColumnCss(i));
                }

                if (getModel().getId(i) != null) {
                    td.setId(getModel().getId(i));
                }
                if (getModel().getTitle(i) != null) {
                    td.addAttribute("title", getModel().getTitle(i));
                }

                if (getModel().getColspan(i) != 1) {
                    td.addAttribute("colspan", String.valueOf(getModel().getColspan(i)));
                }
                tr.add(td);
            }
            tbody.add(tr);
        }
        add(tbody);
    }

    private void generateHeader() {
        if (getModel().hasHeader()) {
            final HtmlGenericElement thead = new HtmlGenericElement("thead");
            final HtmlGenericElement tr = new HtmlGenericElement("tr");
            columnCount = getModel().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                final HtmlGenericElement th = new HtmlGenericElement("th");
                if (getModel().getTitle(i) != null) {
                    th.addAttribute("title", getModel().getTitle(i));
                }
                th.add(getModel().getHeader(i));
                tr.add(th);
            }
            thead.add(tr);
            add(thead);
        }
    }

    protected HtmlTableModel getModel() {
        return model;
    }

    public static abstract class HtmlTableModel {
        public abstract int getColumnCount();

        public abstract HtmlNode getHeader(int column);

        public abstract HtmlNode getBody(int column);

        /**
         * Switch to next line and indicate if the next line exist
         * 
         * @return true if there is more line
         */
        public abstract boolean next();

        public boolean hasHeader() {
            return true;
        }

        /**
         * @param column The column number on which we want to get the css
         */
        public String getColumnCss(final int column) {
            return null;
        }

        public String getLineCss() {
            return null;
        }

        public String getLineId() {
            return null;
        }

        public int getColspan(final int column) {
            return 1;
        }

        public String getId(final int column) {
            return null;
        }

        public String getTitle(int column) {
            return null;
        }

    }

    public static class HtmlLineTableModel extends HtmlTableModel {

        private final List<HtmlTableLine> lines = new ArrayList<HtmlTableLine>();
        int currentLine = -1;
        private HtmlTableLine headerLine;

        public void addLine(final HtmlTableLine line) {
            lines.add(line);
        }

        @Override
        public int getColumnCount() {
            
            if(currentLine < 0) {
                return headerLine.getCells().size();
            }
            
            if (lines.size() > currentLine) {
                return lines.get(currentLine).getCells().size();
            }
            return 0;
        }

        @Override
        public HtmlNode getHeader(final int column) {
            return headerLine.getCells().get(column).getBody();
        }

        @Override
        public HtmlNode getBody(final int column) {
            if (lines.size() > currentLine) {
                return lines.get(currentLine).getCells().get(column).getBody();
            }
            return null;
        }

        @Override
        public boolean next() {
            currentLine++;
            if (lines.size() > currentLine) {
                return true;
            }
            return false;
        }

        @Override
        public boolean hasHeader() {
            return (headerLine != null);
        }

        public void setHeaderLine(HtmlTableLine headerLine) {
            this.headerLine = headerLine;
        }

        @Override
        public String getColumnCss(final int column) {
            return lines.get(currentLine).getCells().get(column).getCss();
        }

        @Override
        public String getId(final int column) {
            return lines.get(currentLine).getCells().get(column).getId();
        }

        @Override
        public int getColspan(final int column) {
            return lines.get(currentLine).getCells().get(column).getColspan();
        }

        @Override
        public String getLineCss() {
            return lines.get(currentLine).getCssClass();
        }

        @Override
        public String getLineId() {
            return lines.get(currentLine).getId();
        }

        public static class HtmlTableLine extends HtmlElement {
            private final List<HtmlTableCell> cells = new ArrayList<HtmlTableCell>();
            private String css = null;

            public HtmlTableLine() {
                super("tr");
            }

            final public String getCssClass() {
                return css;
            }

            @Override
            final public HtmlElement setCssClass(final String css) {
                this.css = css;
                return this;
            }

            final public void addCell(final HtmlTableCell cell) {
                cells.add(cell);
            }

            protected void addTextCell(final String text) {
                addCell(new HtmlTableCell("") {

                    @Override
                    public HtmlNode getBody() {
                        return new HtmlText(text);
                    }
                });
            }

            final public List<HtmlTableCell> getCells() {
                return cells;
            }

            @Override
            public boolean selfClosable() {
                return false;
            }
        }

        public static abstract class HtmlTableCell extends HtmlElement {

            private final String css;
            private String id = null;

            public HtmlTableCell(final String css) {
                super("td");
                this.css = css;
            }

            public abstract HtmlNode getBody();

            public String getCss() {
                return css;
            }

            public int getColspan() {
                return 1;
            }

            @Override
            public HtmlElement setId(final String id) {
                this.id = id;
                return this;
            }

            @Override
            public String getId() {
                return id;
            }

            @Override
            public boolean selfClosable() {
                return false;
            }
        }

        public static class EmptyCell extends HtmlTableCell {

            public EmptyCell() {
                super("");
            }

            @Override
            public HtmlNode getBody() {
                return new PlaceHolderElement();
            }

        }

    }

}
