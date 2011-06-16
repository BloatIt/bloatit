package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class UrlDescription extends ClassDescription {
    private UrlDescription father;
    private final String className;
    private final boolean isAction;
    private final ComponentDescription component;

    public UrlDescription(final ComponentDescription component, final Element element, final boolean isAction) {
        this.isAction = isAction;
        this.component = component;
        className = element.getSimpleName().toString() + "Url";
    }

    public final UrlDescription getFather() {
        return father;
    }

    public final void setFather(final UrlDescription father) {
        this.father = father;
    }

    public final String getClassName() {
        return className;
    }

    public final ComponentDescription getComponent() {
        return component;
    }

    public boolean isAction() {
        return isAction;
    }

    public List<ParameterDescription> getFathersConstructorParameters() {
        final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
        if (father != null) {
            parameters.addAll(father.getFathersConstructorParameters());
            parameters.addAll(father.getConstructorParameters());
        }
        return parameters;
    }

    public List<ParameterDescription> getConstructorParameters() {
        final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
        for (final ParameterDescription params : component.getParameters()) {
            if (!params.isOptional() && (params.getRealRole() == Role.GET || params.getRealRole() == Role.PAGENAME)) {
                parameters.add(params);
            }
        }
        return parameters;
    }

}
