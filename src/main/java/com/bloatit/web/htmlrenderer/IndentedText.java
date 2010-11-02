/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.htmlrenderer;

class IndentedText {
    private int indentCount;
    private String text;
    private final String indentSeparator;
    private final String lineSeparator;

    public IndentedText() {
        this("  ", "\n");
    }

    private IndentedText(String indentSeparator, String lineSeparator) {
        this.indentSeparator = indentSeparator;
        this.lineSeparator = lineSeparator;
        this.indentCount = 0;
        this.text = "";
    }

    public void write(String newText) {
        for (int i = 0; i < this.indentCount; i++) {
            this.text += this.indentSeparator;
        }
        this.text += newText;
        this.text += this.lineSeparator;
    }

    public void indent() {
        this.indentCount += 1;
    }

    public void unindent() {
        this.indentCount -= 1;
    }

    public String getText() {
        return text;
    }

    public boolean hasContent() {
        return !text.isEmpty();
    }
}
