package com.bloatit.framework.webserver.components.form;

import java.math.BigDecimal;

/**
 * <p>
 * A field used to ask the user to input an amount of money
 * </p>
 */
public class HtmlMoneyField extends HtmlFormField<BigDecimal> {

    /**
     * <p>
     * Creates a money field with a given <code>name</code>
     * </p>
     * 
     * @param name
     *            the value of the html attribute <code>name</code>
     */
    public HtmlMoneyField(final String name) {
        super(new HtmlSimpleInput("text"), name);
    }

    /**
     * <p>
     * Creates a money field with a given <code>name</code> and some text used
     * to explain the usage of the field
     * </p>
     * 
     * @param name
     *            the value of the html attribute <code>name</code>
     * @param label
     *            some text displayed to explain how to use the field
     */
    public HtmlMoneyField(final String name, final String label) {
        super(new HtmlSimpleInput("text"), name, label);
    }

    /**
     * <p>
     * Creates a money field based on some form data along some text used to
     * explain the usage of the field
     * </p>
     * 
     * @param data
     *            the form data to base the field on
     * @param label
     *            some text displayed to explain how to use the field
     */
    public HtmlMoneyField(final FormFieldData<BigDecimal> data, final String label) {
        super(new HtmlSimpleInput("text"), data.getFieldName(), label);
        setDefaultValue(data);
        addErrorMessages(data.getFieldMessages());
    }

    @Override
    protected void doSetDefaultValue(BigDecimal value) {
        addAttribute("value", value.toPlainString());
    }

    @Override
    protected void doSetDefaultValue(String defaultValueAsString) {
        addAttribute("value", defaultValueAsString);
    }
}
