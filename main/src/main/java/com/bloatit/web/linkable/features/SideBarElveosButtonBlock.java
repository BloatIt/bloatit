package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.common.TemplateFile;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Feature;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;

public class SideBarElveosButtonBlock extends SideBarElementLayout {

    private final Feature feature;

    public SideBarElveosButtonBlock(final Feature feature) {

        this.feature = feature;
        add(new HtmlTitle(Context.tr("Link it in your site"), 1));
        add(generateForm());
        add(generateScript());
    }

    private XmlNode generateForm() {
        final HtmlDiv item = new HtmlDiv("elveos_button_generator");

        final HtmlDiv outputExample = new HtmlDiv();
        outputExample.setId("elveos_button_generator_output_example");
        item.add(outputExample);

        final HtmlLink configureSize = new HtmlLink("#", Context.tr("+ configure size"));
        configureSize.setCssClass("elveos_button_generator_configure_link");
        item.add(configureSize);

        final HtmlTextArea outputCode = new HtmlTextArea("elveos_button_generator_output", 2, 50);
        outputCode.setId("elveos_button_generator_output");
        item.add(outputCode);

        final HtmlDiv sizeBlock = new HtmlDiv();
        item.add(sizeBlock);

        final JsShowHide jsShowHide = new JsShowHide(item, false);
        jsShowHide.addActuator(configureSize);
        jsShowHide.addListener(sizeBlock);
        jsShowHide.apply();

        final HtmlDiv lengthBlock = new HtmlDiv("elveos_button_generator_configure_length");
        sizeBlock.add(lengthBlock);
        final HtmlRadioButtonGroup lengthRadioGroup = new HtmlRadioButtonGroup("elveos_button_length");
        lengthRadioGroup.addRadioButton("long", tr("Text and logo"));
        lengthRadioGroup.addRadioButton("short", tr("Logo only"));
        lengthRadioGroup.setDefaultValue("long");
        lengthBlock.add(lengthRadioGroup);

        final HtmlDiv heightBlock = new HtmlDiv("elveos_button_generator_configure_heigth");
        sizeBlock.add(heightBlock);
        final HtmlRadioButtonGroup heightRadioGroup = new HtmlRadioButtonGroup("elveos_button_height");
        heightRadioGroup.addRadioButton("small", tr("Small"));
        heightRadioGroup.addRadioButton("medium", tr("Medium"));
        heightRadioGroup.addRadioButton("big", tr("Big"));
        heightRadioGroup.setDefaultValue("small");
        heightBlock.add(heightRadioGroup);

        return item;
    }

    private XmlNode generateScript() {

        final HtmlScript script = new HtmlScript();

        final TemplateFile quotationUpdateScriptTemplate = new TemplateFile("elveos_button_generator.js");
        quotationUpdateScriptTemplate.addNamedParameter("feature_id", String.valueOf(feature.getId()));
        quotationUpdateScriptTemplate.addNamedParameter("protocol", (FrameworkConfiguration.isHttpsEnabled() ? "https" : "http"));

        try {
            script.append(quotationUpdateScriptTemplate.getContent(null));
        } catch (final IOException e) {
            Log.web().error("Fail to generate elveos button generation script", e);
        }
        return script;
    }

}
