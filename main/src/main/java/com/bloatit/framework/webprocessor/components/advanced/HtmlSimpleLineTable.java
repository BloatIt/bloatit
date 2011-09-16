package com.bloatit.framework.webprocessor.components.advanced;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlLeaf;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlText;

/**
 * A simple table that gets constructed simply by adding elements
 */
public class HtmlSimpleLineTable extends HtmlLeaf {
    private final HtmlGenericElement body = new HtmlGenericElement("tbody");
    private final PlaceHolderElement headerPh = new PlaceHolderElement();

    public HtmlSimpleLineTable() {
        super("table");
        add(headerPh);
        add(body);
    }

    public void addLine(final Object... line) {
        final HtmlGenericElement tr = new HtmlGenericElement("tr");
        body.add(tr);
        for (final Object elem : line) {
            HtmlNode element;
            if (elem == null) {
                tr.add(new HtmlGenericElement("td"));
            } else {
                if (elem instanceof String) {
                    element = new HtmlText((String) elem);
                } else if (elem instanceof HtmlNode) {
                    element = (HtmlNode) elem;
                } else if (elem instanceof BigDecimal) {
                    element = new HtmlText(((BigDecimal) elem).toPlainString());
                } else if (elem instanceof Enum<?>) {
                    element = new HtmlText(elem.toString());
                } else {
                    throw new BadProgrammerException("Unexpected non string, non XmlNode in SimpleLineTable. (" + elem + ").");
                }

                tr.add(new HtmlGenericElement("td").add(element));
            }
        }
    }

    public void addHeader(final String... header) {
        final HtmlGenericElement thead = new HtmlGenericElement("thead");
        headerPh.add(thead);
        final HtmlGenericElement tr = new HtmlGenericElement("tr");
        thead.add(tr);
        for (final String elem : header) {
            tr.add(new HtmlGenericElement("th").addText(elem));
        }
    }
}
