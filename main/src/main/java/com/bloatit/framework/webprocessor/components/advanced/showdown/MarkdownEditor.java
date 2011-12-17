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
package com.bloatit.framework.webprocessor.components.advanced.showdown;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;

/**
 * A text area built to input markdown
 * <p>
 * A MarkdownEditor is a special text area that can be used to ease input of
 * markdown text. It is a simple text area when used alone, but when used with a
 * {@link MarkdownPreviewer}, it becomes a powerful markdown editor offering a
 * preview and a button bar helping the user to format his markdown content.
 * </p>
 */
public class MarkdownEditor extends HtmlFormField {
    public MarkdownEditor(final String name, final int rows, final int cols) {
        super(new MarkdownEditorInputBlock(name, rows, cols), name);
    }

    public MarkdownEditor(final String name, final String label, final int rows, final int cols) {
        super(new MarkdownEditorInputBlock(name, rows, cols), name);
        setLabel(label);
    }

    public String getButtonBarId() {
        return ((MarkdownEditorInputBlock) inputBlock).getButtonBarId();
    }

    public String getInputId() {
        if (inputBlock.getInputElement() == null) {
            inputBlock.getInputElement().setId("blmdedit-" + RandomStringUtils.randomAlphabetic(4));
        }
        return inputBlock.getInputElement().getId();
    }

    @Override
    protected List<String> getCustomJs() {
        final List<String> js = new ArrayList<String>();
        js.add(FrameworkConfiguration.getJsShowdownUi());
        return js;
    }

    private static class MarkdownEditorInputBlock extends InputBlock {
        private final HtmlGenericElement input;
        private final HtmlDiv buttonBar = new HtmlDiv("md_button");
        private final HtmlDiv container = new HtmlDiv("md_editor");

        private MarkdownEditorInputBlock(final String name, final int rows, final int cols) {
            input = new HtmlGenericElement("textarea");
            input.addAttribute("cols", String.valueOf(cols));
            input.addAttribute("rows", String.valueOf(rows));
            input.addAttribute("name", name);
            input.addAttribute("id", "wmd-input");
            generate();
        }

        private void generate() {
            buttonBar.setId("wmd-button-bar");
            container.add(buttonBar);
            container.add(input);
        }

        public String getButtonBarId() {
            return buttonBar.getId();
        }

        @Override
        public HtmlGenericElement getInputElement() {
            return input;
        }

        @Override
        public HtmlElement getContentElement() {
            return container;
        }
    }

    @Override
    protected void doSetDefaultStringValue(String value) {
        ((HtmlGenericElement) getInputBlock().getInputElement()).addText(value);
    }
}
