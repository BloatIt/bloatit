package com.bloatit.framework.utils.parsers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.bloatit.framework.exceptions.FatalErrorException;

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
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine jsEngine = manager.getEngineByName("js");

        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(SHOWDOWN_CLASSPATH);
            Reader read = new InputStreamReader(in);
            Reader showdownSrc = new BufferedReader(read);
            jsEngine.eval(showdownSrc);
            showdownConverter = jsEngine.eval("new Showdown.converter()");
            engine = (Invocable) jsEngine;
        } catch (ScriptException e) {
            throw new FatalErrorException("Javascript is not available as a language on your system. Please do something ...", e);
        }
    }

    @Override
    public String parse(String toParse) throws ParsingException {
        try {
            return engine.invokeMethod(showdownConverter, "makeHtml", toParse) + "";
        } catch (ScriptException e) {
            throw new ParsingException("An error happened during the script execution", e);
        } catch (NoSuchMethodException e) {
            throw new ParsingException("The parsing method does not exist", e);
        }
    }
}
