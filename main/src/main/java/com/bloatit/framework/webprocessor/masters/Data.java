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
package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.xcgiserver.HttpResponse;

public abstract class Data implements Linkable {

    protected static final Url NO_ERROR = null;


    public Data(final Url url) throws PageNotFoundException {
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }
    }

    @Override
    final public void writeToHttp(final HttpResponse response, final WebProcessor server) throws RedirectException, IOException {

        final Url checkParameters = checkRightsAndEverything();
        if (checkParameters != NO_ERROR) {
            throw new RedirectException(checkParameters);
        }
        
        doProcess();

        response.writeData(getFileData(), getFileSize(), getFileName());
    }

    /**
     * <p>
     * The url system perform some checks on constraints. You may want to add
     * more specific constraint checking by overriding this method.
     * </p>
     * 
     * @return null if there is no error, the url where you want to be
     *         redirected otherwise.
     */
    protected abstract Url checkRightsAndEverything();

    protected abstract void doProcess();
    
    public abstract byte[] getFileData();

    public abstract long getFileSize();

    public abstract String getFileName();

}
