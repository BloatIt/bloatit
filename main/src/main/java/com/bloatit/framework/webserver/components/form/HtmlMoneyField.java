package com.bloatit.framework.webserver.components.form;

import java.math.BigDecimal;

import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.meta.HtmlText;

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
     * @param name the value of the html attribute <code>name</code>
     */
    public HtmlMoneyField(final String name) {
        super(generateInputField(), name);

    }

    public static HtmlDiv generateInputField() {
        HtmlDiv styleInputDiv = new HtmlDiv("money_input");
        styleInputDiv.add(new HtmlSimpleInput("text"));
        styleInputDiv.add(new HtmlText("€"));

        return styleInputDiv;
    }

    /**
     * <p>
     * Creates a money field with a given <code>name</code> and some text used
     * to explain the usage of the field
     * </p>
     *
     * @param name the value of the html attribute <code>name</code>
     * @param label some text displayed to explain how to use the field
     */
    public HtmlMoneyField(final String name, final String label) {
        super(generateInputField(), name, label);
    }


    /**
     * <p>
     * Creates a money field based on some form data along some text used to
     * explain the usage of the field
     * </p>
     *
     * @param data the form data to base the field on
     * @param label some text displayed to explain how to use the field
     */
    public HtmlMoneyField(final FieldData data, final String label) {
        super(generateInputField(), data.getFieldName(), label);
        setDefaultValue(data);
        addErrorMessages(data.getErrorMessages());
    }

    @Override
    protected void doSetDefaultValue(final BigDecimal value) {
        addAttribute("value", value.toPlainString());
    }

    @Override
    protected void doSetDefaultValue(final String defaultValueAsString) {
        addAttribute("value", defaultValueAsString);
    }
}
