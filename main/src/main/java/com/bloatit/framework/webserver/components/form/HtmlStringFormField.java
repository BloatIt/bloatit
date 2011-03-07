package com.bloatit.framework.webserver.components.form;

public abstract class HtmlStringFormField extends HtmlFormField<String> {

    protected HtmlStringFormField(final InputBlock inputBlock, final String name) {
        super(inputBlock, name);
    }

    protected HtmlStringFormField(final InputBlock inputBlock, final String name, final LabelPosition position) {
        super(inputBlock, name, position);
    }

    protected HtmlStringFormField(final InputBlock inputBlock, final String name, final String label, final LabelPosition position) {
        super(inputBlock, name, label, position);
    }

    protected HtmlStringFormField(final InputBlock inputBlock, final String name, final String label) {
        super(inputBlock, name, label);
    }

    @Override
    protected final void doSetDefaultStringValue(final String value) {
        doSetDefaultValue(value);
    }
}
