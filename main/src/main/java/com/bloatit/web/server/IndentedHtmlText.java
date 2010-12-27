package com.bloatit.web.server;

public abstract class IndentedHtmlText implements Text {
    private int indentCount;
    private final String indentSeparator;
    private final String lineSeparator;

    public IndentedHtmlText() {
        this.indentSeparator = "  ";
        this.lineSeparator = "\n";
        indentCount = 0;
    }

    protected abstract void append(String text);

    /*
     * (non-Javadoc)
     * @see test.IndentableText#write(java.lang.String)
     */
    @Override
    public void writeLine(final String newText) {
        for (int i = 0; i < indentCount; i++) {
            append(indentSeparator);
        }
        append(newText);
        append(lineSeparator);
    }

    /*
     * (non-Javadoc)
     * @see test.IndentableText#indent()
     */
    @Override
    public void indent() {
        indentCount += 1;
    }

    /*
     * (non-Javadoc)
     * @see test.IndentableText#unindent()
     */
    @Override
    public void unindent() {
        indentCount -= 1;
    }

}
