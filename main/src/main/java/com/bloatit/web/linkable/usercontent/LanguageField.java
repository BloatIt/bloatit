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
package com.bloatit.web.linkable.usercontent;

import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.url.UserContentActionUrl;

public class LanguageField extends PlaceHolderElement {

    protected LanguageField(final UserContentActionUrl targetUrl, final String label, final String comment) {
        super();
        final FieldData localeData = targetUrl.getLocaleParameter().pickFieldData();
        final LanguageSelector localeInput = new LanguageSelector(localeData.getName(), label);
        localeInput.setDefaultValue(localeData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        localeInput.addErrorMessages(localeData.getErrorMessages());
        localeInput.setComment(comment);
        add(localeInput);
    }

}
