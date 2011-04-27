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

import java.io.File;
import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;

public abstract class Resource implements Linkable {

    private final static String FILE_STORAGE_DIRECTORY = FrameworkConfiguration.getRessourcesDirStorage();

    @Override
    final public void writeToHttp(final HttpResponse response, final WebProcessor server) throws RedirectException, IOException {

        final File file = new File(getFileUrl());

        if (!file.exists()) {
            Log.web().error("File not exist: '" + file.getParent() + "'.");
            throw new RedirectException(new PageNotFoundUrl());
        }

        if (!file.getParent().equals(FILE_STORAGE_DIRECTORY)) {
            Log.web().error("Invalid stored file directory: '" + file.getParent() + "' instead of '" + FILE_STORAGE_DIRECTORY + "' expected.");
            throw new RedirectException(new PageNotFoundUrl());
        }

        response.writeResource(file.getPath(), getFileSize(), getFileName());
    }

    public abstract String getFileUrl();

    public abstract long getFileSize();

    public abstract String getFileName();

}
