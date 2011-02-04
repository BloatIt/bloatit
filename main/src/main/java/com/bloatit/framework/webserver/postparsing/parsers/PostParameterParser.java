package com.bloatit.framework.webserver.postparsing.parsers;

import java.io.IOException;

import com.bloatit.framework.webserver.mime.InvalidMimeEncodingException;
import com.bloatit.framework.webserver.mime.MalformedMimeException;
import com.bloatit.framework.webserver.postparsing.PostParameter;
import com.bloatit.framework.webserver.postparsing.exceptions.MalformedPostException;

public abstract class PostParameterParser {
    public abstract PostParameter readNext() throws IOException, InvalidMimeEncodingException, MalformedMimeException, MalformedPostException;
}
