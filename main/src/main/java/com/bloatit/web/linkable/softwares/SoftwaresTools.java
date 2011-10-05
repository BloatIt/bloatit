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
package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.bloatit.common.Log;
import com.bloatit.common.TemplateFile;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDownElement;
import com.bloatit.framework.webprocessor.components.form.HtmlSimpleInput;
import com.bloatit.framework.webprocessor.components.form.HtmlSimpleInput.InputType;
import com.bloatit.framework.webprocessor.components.form.HtmlStringFormField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Image;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.linkable.money.QuotationEntry;
import com.bloatit.web.url.ChangePrepaidAmountActionUrl;
import com.bloatit.web.url.FileResourceUrl;
import com.bloatit.web.url.SoftwarePageUrl;

public class SoftwaresTools {

    public static class Logo extends HtmlDiv {
        public Logo(final Software software) {
            super("software_logo_block");
            if (software == null || software.getImage() == null) {
                add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo"));
            } else {
                final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
                add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo"));
            }
        }
    }

    public static class SmallLogo extends HtmlDiv {
        public SmallLogo(final Software software) {
            super("software_logo_block");
            if (software == null || software.getImage() == null) {
                add(new HtmlImage(new Image(WebConfiguration.getImgSoftwareNoLogo()), tr("Software logo"), "software_logo_small"));
            } else {
                final FileResourceUrl imageUrl = new FileResourceUrl(software.getImage());
                add(new HtmlImage(imageUrl, tr("Software logo"), "software_logo_small"));
            }
        }
    }

    public static class Link extends HtmlSpan {
        public Link(final Software software) {
            super("software_link");
            if (software != null) {
                add(new SoftwarePageUrl(software).getHtmlLink(software.getName()));
            } else {
                addText(Context.tr("No software"));
            }
        }
    }

    public static class SoftwareChooserElement extends HtmlStringFormField {

        public SoftwareChooserElement(final String name) {
            super(new SoftwareInputBlock(), name);
            initSoftwareChooser();
        }

        public SoftwareChooserElement(final String name, final String label) {
            super(new SoftwareInputBlock(), name, label);
            initSoftwareChooser();
        }

        public void initSoftwareChooser() {
            setComment(Context.tr("On what software do you want to have this feature. Select 'new software' if your feature is the creation of a new software."));
        }

        /**
         * Sets the default value of the drop down menu
         * <p>
         * The default value is set based on the <i>value</i> field of the
         * {@link #addDropDownElement(String, String)} method (the code which is
         * not visible from the user).
         * </p>
         * 
         * @param value the code of the default element
         */
        @Override
        protected void doSetDefaultValue(final String value) {
            SoftwareInputBlock softwareInputBlock = (SoftwareInputBlock) getInputBlock();
            softwareInputBlock.setDefaultValue(value);

        }

        static class SoftwareInputBlock extends InputBlock {

            private final Map<String, HtmlDropDownElement> elements = new HashMap<String, HtmlDropDownElement>();
            private HtmlGenericElement failbackSelectElement;
            private HtmlDiv softwareChooserBlock;

            public SoftwareInputBlock() {
                softwareChooserBlock = new HtmlDiv("software_chooser_block");
                softwareChooserBlock.setId("software_chooser_block_id");

                // New software checkbox
                HtmlDiv newSoftwareCheckBoxBlock = new HtmlDiv("new_software_checkbox_block");
                newSoftwareCheckBoxBlock.addAttribute("style", "display:none;");
                softwareChooserBlock.add(newSoftwareCheckBoxBlock);

                HtmlSimpleInput checkboxInput = new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.CHECKBOX_INPUT));
                checkboxInput.setId("software_chooser_checkbox_id");
                newSoftwareCheckBoxBlock.add(checkboxInput);
                newSoftwareCheckBoxBlock.addText(Context.tr("The feature is related to a new software."));

                // Search input
                HtmlElement searchSoftwareInput = new HtmlSimpleInput(HtmlSimpleInput.getInput(InputType.TEXT_INPUT));
                searchSoftwareInput.setId("software_chooser_search_id");
                searchSoftwareInput.addAttribute("style", "display:none;");
                searchSoftwareInput.addAttribute("placeholder", Context.tr("Choose a software"));
                searchSoftwareInput.addAttribute("autocomplete", "off");
                softwareChooserBlock.add(searchSoftwareInput);

                failbackSelectElement = new HtmlGenericElement("select");
                failbackSelectElement.setId("software_chooser_failback");

                addDropDownElement("", Context.tr("Select a software")).setDisabled().setSelected();
                addDropDownElement("", Context.tr("New software"));

                StringBuilder jsSoftwareNameList = new StringBuilder("[");
                StringBuilder jsSoftwareIdList = new StringBuilder("[");

                for (final Software software : SoftwareManager.getAll()) {
                    addDropDownElement(String.valueOf(software.getId()), software.getName());
                    jsSoftwareNameList.append("\"");
                    jsSoftwareNameList.append(software.getName());
                    jsSoftwareNameList.append("\",");

                    jsSoftwareIdList.append("\"");
                    jsSoftwareIdList.append(software.getId());
                    jsSoftwareIdList.append("\",");
                }
                jsSoftwareNameList.append("]");
                jsSoftwareIdList.append("]");

                softwareChooserBlock.add(failbackSelectElement);

                // Add js
                final HtmlScript softwareChooserScript = new HtmlScript();

                final TemplateFile softwareChooserScriptTemplate = new TemplateFile("software_chooser.js");
                softwareChooserScriptTemplate.addNamedParameter("software_name_list", jsSoftwareNameList.toString());
                softwareChooserScriptTemplate.addNamedParameter("software_id_list", jsSoftwareIdList.toString());

                try {
                    softwareChooserScript.append(softwareChooserScriptTemplate.getContent(null));
                } catch (final IOException e) {
                    Log.web().error("Fail to generate software chooser script", e);
                }

                softwareChooserBlock.add(softwareChooserScript);

            }

            @Override
            public HtmlElement getInputElement() {
                return failbackSelectElement;
            }

            public void setDefaultValue(String value) {
                final HtmlDropDownElement checkedElement = elements.get(value);
                if (checkedElement != null) {
                    checkedElement.addAttribute("selected", "selected");
                }

            }

            @Override
            public HtmlElement getContentElement() {
                return softwareChooserBlock;
            }

            public HtmlDropDownElement addDropDownElement(final String value, final String displayName) {
                final HtmlDropDownElement opt = new HtmlDropDownElement();
                opt.addText(displayName);
                opt.addAttribute("value", value);
                failbackSelectElement.add(opt);
                elements.put(value, opt);
                return opt;
            }

        }

    }

}
