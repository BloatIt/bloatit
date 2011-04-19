package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class UrlComponentDescription {

    private final String className;
    private final String attributeName;
    private final boolean isComponent;
    private final String codeName;
    private final Set<UrlComponentDescription> children = new HashSet<UrlComponentDescription>();

    private final Map<String, ParameterDescription> parameters = new HashMap<String, ParameterDescription>();

    public UrlComponentDescription(final Element element, final ParamContainer container, final String attributeName) {
        className = element.getSimpleName().toString() + "UrlComponent";
        isComponent = container.isComponent();
        codeName = container.value();
        this.attributeName = attributeName;
    }

    public final String getClassName() {
        return className;
    }

    public final boolean isComponent() {
        return isComponent;
    }
    
    public final String getAttributeName() {
        if (attributeName == null) {
            throw new RuntimeException();
        }
        return attributeName;
    }

    public final String getCodeNameStr() {
        return Utils.getStr(codeName);
    }

    public final Map<String, ParameterDescription> getParameters() {
        return parameters;
    }

    public void addParameter(final ParameterDescription description) {
        parameters.put(description.getAttributeName(), description);
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

    public Set<UrlComponentDescription> getSubComponents() {
        return children;
    }

}
