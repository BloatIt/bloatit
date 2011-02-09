package com.bloatit.framework.utils.parsers;

public interface Parser {

    /**
     * Parses some text into another text format
     * 
     * @param toParse
     *            the text to parse
     * @return the parsed text
     * @throws ParsingException
     *             whenever an error occurs during parsing
     */
    String parse(String toParse) throws ParsingException;
}
