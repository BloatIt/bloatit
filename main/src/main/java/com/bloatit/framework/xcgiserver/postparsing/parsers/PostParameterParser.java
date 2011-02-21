package com.bloatit.framework.xcgiserver.postparsing.parsers;

import java.io.EOFException;
import java.io.IOException;

import com.bloatit.framework.xcgiserver.mime.InvalidMimeEncodingException;
import com.bloatit.framework.xcgiserver.mime.MalformedMimeException;
import com.bloatit.framework.xcgiserver.postparsing.PostParameter;
import com.bloatit.framework.xcgiserver.postparsing.exceptions.MalformedPostException;

/**
 * <p>
 * Class that encompasses various PostParsers
 * </p>
 */
public abstract class PostParameterParser {
    /**
     * Finds the next POST data and return it as a <code>PostParameter</code>
     * 
     * @return the <code>PostParameter</code> representing the POST data
     * @throws EOFException If EOF is reached too early. Parsing should stop
     *             after this occur
     * @throws IOException If an IO error occurs, parsing should stop after this
     *             occur
     * @throws InvalidMimeEncodingException If Mime encoding used is not valid.
     *             Parsing can continue after this happen
     * @throws MalformedMimeException If Mime is malformed. Parsing may continue
     *             after this happen
     * @throws MalformedPostException If post is malformed. Parsing should stop
     *             after this happen
     */
    public abstract PostParameter readNext() throws EOFException,
                                            IOException,
                                            InvalidMimeEncodingException,
                                            MalformedMimeException,
                                            MalformedPostException;
}
