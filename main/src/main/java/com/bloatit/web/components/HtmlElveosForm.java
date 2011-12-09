package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.linkable.usercontent.AsTeamField;

public class HtmlElveosForm extends HtmlForm {

    private final HtmlDiv header;
    private final PlaceHolderElement body;
    private final PlaceHolderElement languageComment;
    private final PlaceHolderElement submits;

    public HtmlElveosForm(final String target) {
        this(target, true, Method.POST);
    }

    public HtmlElveosForm(final String target, final boolean showBody, final Method method) {
        super(target, method);
        header = new HtmlDiv("form-header");
        submits = new PlaceHolderElement();
        body = new PlaceHolderElement();
        languageComment = new PlaceHolderElement();

        final HtmlDiv bodyblock = new HtmlDiv("form-body");
        final HtmlDiv submitsBlock = new HtmlDiv("form-submits");

        if (showBody) {
            bodyblock.setCssClass("form-show-body");
        }

        super.add(header);
        super.add(bodyblock);
        bodyblock.add(body);
        body.add(languageComment);
        bodyblock.add(new HtmlClearer());
        bodyblock.add(submitsBlock);
        submitsBlock.add(submits);
        bodyblock.add(new HtmlClearer());
    }

    public HtmlElveosForm(final String target, final boolean b) {
        this(target, b, Method.POST);
    }

    public void addLanguageChooser(final String name, final String defaultLang) {
        final HtmlDiv divLang = new HtmlDiv("fheader-first-line");
        header.add(divLang);
        divLang.add(new HtmlDiv("fheader-label").addText(Context.tr("I'm filling this form in")));
        final LanguageSelector langselector = new LanguageSelector(name);
        divLang.add(langselector);
        langselector.setDefaultValue(defaultLang);

        final HtmlDiv divNotEn = new HtmlDiv("fheader-second-line");
        divLang.add(divNotEn);
        if (defaultLang != "en") {
            languageComment.add(new HtmlSpan("light-notification").addText("Please consider writing in English, it's the most understood language on the Web."));
        }
    }

    public void addAsTeamField(final AsTeamField f) {
        if (f.showSomething()) {
            final HtmlDiv div = new HtmlDiv("fheader-team");
            div.add(new HtmlDiv("fheader-label").addText(Context.tr("In the name of")));
            div.add(f);
            header.add(div);
        }
    }

    public HtmlBranch addField(final HtmlFormField html) {
        return body.add(html);
    }

    public HtmlBranch addSubmit(final HtmlNode html) {
        return submits.add(html);
    }

    @Override
    public HtmlBranch add(final HtmlNode html) {
        return body.add(html);
    }

}
