package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class UrlComponentDescription {

    private final String className;
    private final boolean isComponent;
    private final String componentName;
    private final List<UrlComponentDescription> children = new ArrayList<UrlComponentDescription>();

    private final Map<String, ParameterDescription> parameters = new HashMap<String, ParameterDescription>();

    public UrlComponentDescription(final Element element, final ParamContainer container) {
        className = element.getSimpleName().toString() + "UrlComponent";
        isComponent = container.isComponent();
        componentName = container.value();
    }

    public final String getClassName() {
        return className;
    }

    public final boolean isComponent() {
        return isComponent;
    }

    public final String getComponentNameStr() {
        return Utils.getStr(componentName);
    }

    public final Map<String, ParameterDescription> getParameters() {
        return parameters;
    }

    public void addParameter(final ParameterDescription description) {
        parameters.put(description.getName(), description);
    }

    public boolean hasUrlParameter() {
        for (final Entry<String, ParameterDescription> ent : parameters.entrySet()) {
            if (!ent.getValue().isOptional() && ent.getValue().getRealRole() == Role.GET) {
                return true;
            }
        }
        return false;
    }

    public void addSubComponent(final UrlComponentDescription child) {
        children.add(child);
    }

}
