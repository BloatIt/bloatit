package com.bloatit.web.linkable.usercontent;

import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.url.UserContentActionUrl;

public class LanguageField extends PlaceHolderElement {

    public LanguageField(final UserContentActionUrl targetUrl, final String label, final String comment) {
        super();
        final FieldData localeData = targetUrl.getLocaleParameter().pickFieldData();
        final LanguageSelector localeInput = new LanguageSelector(localeData.getName(), label);
        localeInput.setDefaultValue(localeData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        localeInput.addErrorMessages(localeData.getErrorMessages());
        localeInput.setComment(comment);
        add(localeInput);
    }

}
