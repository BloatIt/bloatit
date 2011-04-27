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
package com.bloatit.framework.webprocessor.url;

import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;

public final class UrlParameterDescription<U> {

    private final String name;
    private final Class<U> convertInto;
    private final Role role;
    private final String defaultValue;
    private final String suggestedValue;
    private final String conversionErrorMsg;
    private final boolean isOptional;

    public UrlParameterDescription(final String name,
                                   final Class<U> convertInto,
                                   final Role role,
                                   final String defaultValue,
                                   final String suggestedValue,
                                   final String conversionErrorMsg,
                                   final boolean isOptional) {
        super();
        this.name = name;
        this.convertInto = convertInto;
        this.role = role;
        this.defaultValue = defaultValue;
        this.suggestedValue = suggestedValue;
        this.conversionErrorMsg = conversionErrorMsg;
        this.isOptional = isOptional;
    }

    public final String getName() {
        return name;
    }

    public final Class<U> getConvertInto() {
        return convertInto;
    }

    public final Role getRole() {
        return role;
    }

    public final String getDefaultValue() {
        return defaultValue;
    }

    public final String getSuggestedValue() {
        return suggestedValue;
    }

    public final String getConversionErrorMsg() {
        return Context.tr(conversionErrorMsg);
    }

    public boolean isOptional() {
        return isOptional;
    }

}
