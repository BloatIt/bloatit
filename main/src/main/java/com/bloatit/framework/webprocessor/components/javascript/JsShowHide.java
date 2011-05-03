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
package com.bloatit.framework.webprocessor.components.javascript;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlScript;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;

public class JsShowHide {

    private final List<HtmlBranch> actuators = new ArrayList<HtmlBranch>();
    private final List<HtmlElement> listeners = new ArrayList<HtmlElement>();
    private final RandomString rng = new RandomString(10);
    private final boolean state;
    private boolean hasFallback;


    public void setHasFallback(boolean b) {
        this.hasFallback = b;
    }

    public JsShowHide(final boolean state) {
        this.state = state;
        this.hasFallback = true;
    }

    public void addActuator(final HtmlBranch actuator) {
        actuators.add(actuator);

        injectJQueryInclusion(actuator);
    }

    public void addListener(final HtmlElement listener) {
        listeners.add(listener);

    }

    public void apply() {

        if (!state && hasFallback) {
            for (final HtmlElement listener : listeners) {
                listener.addAttribute("style", "display: none;");
            }
        }

        prepareIds();

        for (final HtmlBranch actuator : actuators) {

            final String effectCall = "toggle( \"blind\", 200)";
            HtmlScript script = new HtmlScript();

            script.append("$(function() {\n" + "        function runEffect() {\n");

            for (final HtmlElement listener : listeners) {
                script.append("          $( \"#" + listener.getId() + "\" )." + effectCall + ";\n");
            }
            script.append("        }\n");
            script.append("        $( \"#" + actuator.getId() + "\" ).click(function() {\n" + "            runEffect();\n"
                    + "            return false;\n" + "        });\n");

            for (final HtmlElement listener : listeners) {
                script.append("$( \"#" + listener.getId() + "\" ).hide();\n");
            }

            script.append("    });");

            actuator.add(script);
        }

    }

    private void prepareIds() {
        for (final HtmlElement listener : listeners) {
            if (listener.getId() == null) {
                listener.setId(rng.nextString());
            }
        }

        for (final HtmlBranch actuator : actuators) {
            if (actuator.getId() == null) {
                actuator.setId(rng.nextString());
            }
        }

    }

    private void injectJQueryInclusion(final HtmlBranch actuator) {
        actuator.add(new HtmlGenericElement() {

            @Override
            protected List<String> getCustomJs() {
                final ArrayList<String> customJsList = new ArrayList<String>();
                customJsList.add(FrameworkConfiguration.getJsJquery());
                customJsList.add(FrameworkConfiguration.getJsJqueryUi());
                return customJsList;
            }

        });
    }



}
