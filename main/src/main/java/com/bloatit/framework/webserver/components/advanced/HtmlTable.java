package com.bloatit.framework.webserver.components.advanced;

import com.bloatit.framework.webserver.components.HtmlGenericElement;

public class HtmlTable extends HtmlGenericElement {

    private final HtmlTableModel model;
    private final int colomnCount;

    public HtmlTable(HtmlTableModel model) {
        super("table");
        this.model = model;

        colomnCount = model.getColumnCount();

        generateHeader();
        generateBody();

    }

    private void generateBody() {
        while (model.next()) {
            HtmlGenericElement tr = new HtmlGenericElement("tr");

            for (int i = 0; i < colomnCount; i++) {
                HtmlGenericElement td = new HtmlGenericElement("td");
                td.addText(model.getBody(i));
                tr.add(td);
            }
            add(tr);
        }
    }

    private void generateHeader() {
        HtmlGenericElement tr = new HtmlGenericElement("tr");

        for (int i = 0; i < colomnCount; i++) {
            HtmlGenericElement th = new HtmlGenericElement("th");
            th.addText(model.getHeader(i));
            tr.add(th);
        }
        add(tr);
    }

    public interface HtmlTableModel {

        int getColumnCount();

        String getHeader(int column);

        String getBody(int column);

        boolean next();
    }

}
