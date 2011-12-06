package com.bloatit.web.components;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
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
    private final PlaceHolderElement submits;

    public HtmlElveosForm(String target) {
        this(target, Method.POST);
    }

    public HtmlElveosForm(String target, Method method) {
        super(target, method);
        header = new HtmlDiv("form-header");
        submits = new PlaceHolderElement();
        body = new PlaceHolderElement();
        HtmlDiv bodyblock = new HtmlDiv("form-body");
        HtmlDiv submitsBlock = new HtmlDiv("form-submits");

        super.add(header);
        super.add(bodyblock);
        bodyblock.add(body);
        bodyblock.add(new HtmlClearer());
        bodyblock.add(submitsBlock);
        submitsBlock.add(submits);
        bodyblock.add(new HtmlClearer());
    }

    public void addLanguageChooser(String name, String defaultLang) {
        HtmlDiv divLang = new HtmlDiv("fheader-first-line");
        header.add(divLang);
        divLang.add(new HtmlDiv("fheader-label").addText(Context.tr("I'm filling this form in")));
        LanguageSelector langselector = new LanguageSelector(name);
        divLang.add(langselector);
        langselector.setDefaultValue(defaultLang);

        HtmlDiv divNotEn = new HtmlDiv("fheader-second-line");
        divLang.add(divNotEn);
        if (defaultLang != "en") {
            Context.getSession().notifyWarning(Context.tr("You would have more impact by writing this in English."));
        }

    }

    public void addAsTeamField(AsTeamField f) {
        if (f.showSomething()) {
            HtmlDiv div = new HtmlDiv("fheader-team");
            div.add(new HtmlDiv("fheader-label").addText(Context.tr("In the name of")));
            div.add(f);
            header.add(div);
        }
    }

    public HtmlBranch addField(HtmlFormField html) {
        return body.add(html);
    }

    public HtmlBranch addSubmit(HtmlNode html) {
        return submits.add(html);
    }

    @Override
    public HtmlBranch add(HtmlNode html) {
        return body.add(html);
    }

}
