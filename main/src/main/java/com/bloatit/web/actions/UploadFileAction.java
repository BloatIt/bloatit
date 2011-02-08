/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.UploadFileActionUrl;

/**
 * A response to a form used to create a new idea
 */
@ParamContainer("file/doupload")
public final class UploadFileAction extends Action {


    @RequestParam(name = "wow", role = Role.POST)
    private final String wow;

    @RequestParam(name = "fichier", role = Role.POST)
    private final String fichier;

    private final UploadFileActionUrl url;

    public UploadFileAction(final UploadFileActionUrl url) {
        super(url);
        this.url = url;

        this.wow = url.getWow();
        this.fichier = url.getFichier();

    }

    @Override
    protected Url doProcess() throws RedirectException {

        System.err.println("wow" + wow);
        System.err.println("fichier "+fichier);
        FileMetadataManager.createFromTempFile(session.getAuthToken().getMember(), fichier, fichier, "Test upload");

        return new IndexPageUrl();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());

        return new IndexPageUrl();
    }
}
