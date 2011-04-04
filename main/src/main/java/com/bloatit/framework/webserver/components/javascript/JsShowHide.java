package com.bloatit.framework.webserver.components.javascript;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.utils.RandomString;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.XmlText;

public class JsShowHide {

    private final List<HtmlBranch> actuators = new ArrayList<HtmlBranch>();
    private final List<HtmlBranch> listeners = new ArrayList<HtmlBranch>();
    private final RandomString rng = new RandomString(10);
    private final boolean state;

    public JsShowHide(boolean state) {
        this.state = state;
    }

    public void addActuator(HtmlBranch actuator) {
        actuators.add(actuator);

        injectJQueryInclusion(actuator);
    }

    public void addListener(HtmlBranch listener) {
        listeners.add(listener);

    }

    public void apply() {

        if (!state) {
            for (HtmlBranch listener : listeners) {
                 listener.addAttribute("style", "display: none;");
            }
        }

        prepareIds();

        for (HtmlBranch actuator : actuators) {
            HtmlGenericElement scriptElement = new HtmlGenericElement("script");

            String effectCall = "toggle( \"blind\")";

            String script = "$(function() {\n" + "        function runEffect() {\n";

            for (HtmlBranch listener : listeners) {
                script += "          $( \"#" + listener.getId() + "\" )." + effectCall + ";\n";
            }
            script += "        }\n";

            script += "        $( \"#" + actuator.getId() + "\" ).click(function() {\n" + "            runEffect();\n"
                    + "            return false;\n" + "        });\n";

            for (HtmlBranch listener : listeners) {
                script += "$( \"#" + listener.getId() + "\" ).hide();\n";
            }

            script += "    });";

            scriptElement.add(new XmlText(script));

            actuator.add(scriptElement);
        }

    }

    private void prepareIds() {
        for (HtmlBranch listener : listeners) {
            if (listener.getId() == null) {
                listener.setId(rng.nextString());
            }
        }

        for (HtmlBranch actuator : actuators) {
            if (actuator.getId() == null) {
                actuator.setId(rng.nextString());
            }
        }

    }

    private void injectJQueryInclusion(HtmlBranch actuator) {
        actuator.add(new HtmlGenericElement() {

            @Override
            protected List<String> getCustomJs() {
                ArrayList<String> customJsList = new ArrayList<String>();
                customJsList.add("jquery-1.4.4.min.js");
                customJsList.add("jquery-ui-1.8.10.custom.min.js");
                //customJsList.add("jquery-1.5.1.js");
                //customJsList.add("jquery-ui-1.8.11.custom.js");
                // TODO: add in config file
                return customJsList;
            }

        });
    }

}
