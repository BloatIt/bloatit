package com.bloatit.framework.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bloatit.common.Log;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

public class ResourceServer implements XcgiProcessor {

    private final List<String> resourceDirList;

    public ResourceServer() {
        resourceDirList = new ArrayList<String>();

        resourceDirList.add("/resources");
        resourceDirList.add("/favicon.ico");
    }

    @Override
    public boolean process(HttpHeader header, HttpPost postData, HttpResponse response) throws IOException {

        if (!resourceDirList.contains(header.getScriptName())) {
            return false;
        }

        String path = header.getScriptFilename() + header.getPathInfo();
        File file = new File(path);

        if (!file.exists()) {
            return false;
        }

        Log.resources().trace("Send resource '" + path + "'");

        response.writeResource(path, file.length(), file.getName());

        return true;
    }

    @Override
    public boolean initialize() {
        return true;
    }

}
