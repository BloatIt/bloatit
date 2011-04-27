//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.components.writers;

import java.io.OutputStream;

/**
 * <p>
 * An implementation of an {@link QueryResponseStream} that just display raw
 * text (no indentation, no line breaks ...)
 * </p>
 * <p>
 * Should be used in production environments
 * </p>
 */
public final class SimpleHtmlStream extends QueryResponseStream {

    /**
     * Creates a new SimpleHtmlStream
     * 
     * @param out the underlying stream
     */
    public SimpleHtmlStream(final OutputStream out) {
        super(out);
    }

    @Override
    public void writeLine(final String newText) {
        append(newText);
    }

    /**
     * Nothing
     */
    @Override
    public void writeIndentation() {
        // Do nothing
    }

    /**
     * Nothing
     */
    @Override
    public void writeNewLineChar() {
        // Do nothing
    }

    @Override
    public void writeRawText(final String newText) {
        append(newText);
    }

    /**
     * Nothing
     */
    @Override
    public void indent() {
        // Do nothing
    }

    /**
     * Nothing
     */
    @Override
    public void unindent() {
        // Do nothing
    }
}
