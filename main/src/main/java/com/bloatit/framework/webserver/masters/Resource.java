package com.bloatit.framework.webserver.masters;

import java.io.File;
import java.io.IOException;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;

public abstract class Resource implements Linkable {

    private final static String FILE_STORAGE_DIRECTORY = ConfigurationManager.SHARE_DIR + "file_storage";

    @Override
    final public void writeToHttp(HttpResponse response) throws RedirectException, IOException {

        File file = new File(getFileUrl());

        if(!file.exists()) {
            Log.web().error("File not exist: '"+file.getParent()+"'.");
            throw new RedirectException(new PageNotFoundUrl());
        }


        if(!file.getParent().equals(FILE_STORAGE_DIRECTORY)) {
            Log.web().error("Invalid stored file directory: '"+file.getParent()+"' instead of '"+FILE_STORAGE_DIRECTORY+"' expected.");
            throw new RedirectException(new PageNotFoundUrl());
        }


        response.writeResource(file.getPath(), getFileSize(), getFileName());
    }

    public abstract String getFileUrl();

    public abstract long getFileSize();

    public abstract  String getFileName();


}
