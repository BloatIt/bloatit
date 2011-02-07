package com.bloatit.framework.xcgiserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface XcgiParser {

    Map<String, String> getEnv() throws IOException;

    InputStream getPostStream() throws IOException;

    OutputStream getWriteStream() throws IOException;

}
