package com.bloatit.framework.utils.parsers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.bloatit.framework.exceptions.general.BadProgrammerException;

/**
 * An implementation of a parser that converts markdown to html
 */
public class MarkdownParser implements Parser {

    private Object showdownConverter = null;
    private final Invocable engine;
    private final static String SHOWDOWN_CLASSPATH = "showdown/src/showdown.js";

    /**
     * Creates a markdown parser
     */
    public MarkdownParser() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine jsEngine = manager.getEngineByName("js");

        try {
            final InputStream in = getClass().getClassLoader().getResourceAsStream(SHOWDOWN_CLASSPATH);
            final Reader read = new InputStreamReader(in);
            final Reader showdownSrc = new BufferedReader(read);
            jsEngine.eval(showdownSrc);
            showdownConverter = jsEngine.eval("new Showdown.converter()");
            engine = (Invocable) jsEngine;
        } catch (final ScriptException e) {
            throw new BadProgrammerException("Javascript is not available as a language on your system. Please do something ...", e);
        }
    }

    /**
     * @throws ParsingException when the parser couldn't initialize (note, this
     *             is a system error, not an error linked to the user content)
     */
    @Override
    public String parse(final String toParse) throws ParsingException {
        try {
            return engine.invokeMethod(showdownConverter, "makeHtml", toParse) + "";
        } catch (final ScriptException e) {
            throw new ParsingException("An error happened during the script execution", e);
        } catch (final NoSuchMethodException e) {
            throw new ParsingException("The parsing method does not exist", e);
        }
    }
}
