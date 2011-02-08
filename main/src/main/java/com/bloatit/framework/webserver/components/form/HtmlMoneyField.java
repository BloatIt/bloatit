package com.bloatit.framework.webserver.components.form;

import java.math.BigDecimal;

public class HtmlMoneyField extends HtmlFormField<BigDecimal> {

    public HtmlMoneyField(final String name) {
        super(new HtmlSimpleInput("text"), name);
    }

    public HtmlMoneyField(final String name, final String label) {
        super(new HtmlSimpleInput("text"), name, label);
    }

    @Override
    protected void doSetDefaultValue(BigDecimal value) {
        addAttribute("value", value.toPlainString());
    }

}
