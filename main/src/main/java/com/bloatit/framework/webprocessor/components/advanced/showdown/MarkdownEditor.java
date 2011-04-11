package com.bloatit.framework.webprocessor.components.advanced.showdown;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.form.HtmlStringFormField;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
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
public class MarkdownEditor extends HtmlStringFormField {
    public MarkdownEditor(String name, int rows, int cols) {
        super(new MarkdownEditorInputBlock(name, rows, cols), name);
    }

    public MarkdownEditor(String name, String label, int rows, int cols) {
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
        List<String> js = new ArrayList<String>();
        js.add(FrameworkConfiguration.getJsShowdownUi());
        return js;
    }

    @Override
    protected List<String> getCustomCss() {
        List<String> css = new ArrayList<String>();
        css.add(FrameworkConfiguration.getCssShowdown());
        return css;
    }

    @Override
    protected void doSetDefaultValue(String value) {
        ((HtmlTextArea) inputBlock.getInputElement()).setDefaultValue(value);
    }

    @Override
    public void setComment(final String comment) {
        ((HtmlTextArea) inputBlock.getInputElement()).setComment(comment);
    }
    
    public static class MarkdownEditorInputBlock extends InputBlock {
        private final HtmlTextArea input;
        private final HtmlDiv buttonBar = new HtmlDiv("md_button");
        private final HtmlDiv container = new HtmlDiv("md_editor");

        public MarkdownEditorInputBlock(String name, int rows, int cols) {
            input = new HtmlTextArea(name, rows, cols);
            generate();
        }

        public MarkdownEditorInputBlock(String name, String label, int rows, int cols) {
            input = new HtmlTextArea(name, label, rows, cols);
            generate();
        }

        private void generate() {
            buttonBar.setId("blmdbar-" + RandomStringUtils.randomAlphabetic(4));
            container.add(buttonBar);
            container.add(input);
        }

        public String getButtonBarId() {
            return buttonBar.getId();
        }

        @Override
        public HtmlTextArea getInputElement() {
            return input;
        }

        @Override
        public HtmlElement getContentElement() {
            return container;
        }
    }
}
