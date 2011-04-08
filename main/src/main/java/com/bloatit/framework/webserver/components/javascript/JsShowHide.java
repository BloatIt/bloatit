package com.bloatit.framework.webserver.components.javascript;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.XmlText;

public class JsShowHide {

    private final List<HtmlBranch> actuators = new ArrayList<HtmlBranch>();
    private final List<HtmlBranch> listeners = new ArrayList<HtmlBranch>();
    private final RandomString rng = new RandomString(10);
    private final boolean state;

    public JsShowHide(final boolean state) {
        this.state = state;
    }

    public void addActuator(final HtmlBranch actuator) {
        actuators.add(actuator);

        injectJQueryInclusion(actuator);
    }

    public void addListener(final HtmlBranch listener) {
        listeners.add(listener);

    }

    public void apply() {

        if (!state) {
            for (final HtmlBranch listener : listeners) {
                 listener.addAttribute("style", "display: none;");
            }
        }

        prepareIds();

        for (final HtmlBranch actuator : actuators) {
            final HtmlGenericElement scriptElement = new HtmlGenericElement("script");

            final String effectCall = "toggle( \"blind\")";

            String script = "$(function() {\n" + "        function runEffect() {\n";

            for (final HtmlBranch listener : listeners) {
                script += "          $( \"#" + listener.getId() + "\" )." + effectCall + ";\n";
            }
            script += "        }\n";

            script += "        $( \"#" + actuator.getId() + "\" ).click(function() {\n" + "            runEffect();\n"
                    + "            return false;\n" + "        });\n";

            for (final HtmlBranch listener : listeners) {
                script += "$( \"#" + listener.getId() + "\" ).hide();\n";
            }

            script += "    });";

            scriptElement.add(new XmlText(script));

            actuator.add(scriptElement);
        }

    }

    private void prepareIds() {
        for (final HtmlBranch listener : listeners) {
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
