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

import java.util.Locale;

import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;

public class UrlString extends Url {

    private final String url;

    public UrlString(final String url) {
        super();
        this.url = url;
    }

    @Override
    public Url clone() {
        return new UrlString(url);
    }

    @Override
    public String urlString() {
        return url;
    }
    
    @Override
    public String internalUrlString(boolean multilanguage, Locale forcedLanguage) {
        return url;
    }

    @Override
    protected void doConstructUrl(final StringBuilder sb) {
        // nothing to do here. All the work is done in Url.
    }

    @Override
    public void addParameter(final String key, final String value) {
        // nothing to do here. There is no parameters in UrlStringBinder
    }

    @Override
    public Messages getMessages() {
        return new Messages();
    }

    @Override
    public boolean isAction() {
        return false;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    protected void doGetParametersAsStrings(final Parameters parameters) {
        // nothing to do. There are no parameters.
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.AUTO;
    }

}
