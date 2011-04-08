package com.bloatit.framework.webprocessor.components.advanced;

import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;

/**
 * TODO : Fred has to comment this
 */
public class HtmlTable extends HtmlGenericElement {

    private final HtmlTableModel model;
    private final int columnCount;

    public HtmlTable(final HtmlTableModel model) {
        super("table");
        this.model = model;

        columnCount = model.getColumnCount();

        generateHeader();
        generateBody();

    }

    private void generateBody() {
        while (model.next()) {
            final HtmlGenericElement tr = new HtmlGenericElement("tr");

            for (int i = 0; i < columnCount; i++) {
                final HtmlGenericElement td = new HtmlGenericElement("td");
                td.add(model.getBody(i));
                if (model.getColumnCss(i) != null) {
                    td.setCssClass(model.getColumnCss(i));
                }
                tr.add(td);
            }
            add(tr);
        }
    }

    private void generateHeader() {
        final HtmlGenericElement tr = new HtmlGenericElement("tr");

        for (int i = 0; i < columnCount; i++) {
            final HtmlGenericElement th = new HtmlGenericElement("th");
            th.add(model.getHeader(i));
            tr.add(th);
        }
        add(tr);
    }

    public static abstract class HtmlTableModel {
        public abstract int getColumnCount();

        public abstract XmlNode getHeader(int column);

        public abstract XmlNode getBody(int column);

        public abstract boolean next();

        public boolean hasHeader() {
            return true;
        }

        public String getColumnCss(@SuppressWarnings("unused") final int column) {
            return null;
        }
    }

}
