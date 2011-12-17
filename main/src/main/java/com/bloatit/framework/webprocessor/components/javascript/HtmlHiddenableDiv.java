package com.bloatit.framework.webprocessor.components.javascript;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;

public class HtmlHiddenableDiv extends PlaceHolderElement {

    private final PlaceHolderElement all;
    private final HtmlDiv div;
    private final JsShowHide showHide;

    public HtmlHiddenableDiv(HtmlElement actuator) {
        this(actuator, true, false);
    }

    public HtmlHiddenableDiv(HtmlElement actuator, boolean state) {
        this(actuator, state, false);
    }

    public HtmlHiddenableDiv(HtmlElement actuator, boolean state, boolean hasFallback) {
        this.all = new PlaceHolderElement();
        this.div = new HtmlDiv();

        super.add(all);
        all.add(div);

        showHide = new JsShowHide(all, state);
        showHide.addListener(div);
        showHide.setHasFallback(hasFallback);
        showHide.addActuator(actuator);
        showHide.apply();
    }

    @Override
    public HtmlBranch add(HtmlNode html) {
        return div.add(html);
    }

    @Override
    public HtmlBranch addAttribute(String name, String value) {
        return div.addAttribute(name, value);
    }
}
