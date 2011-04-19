package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class UrlDescription {
    private UrlDescription father;
    private final String className;
    private final boolean isAction;
    private final UrlComponentDescription component;

    public UrlDescription(final Element element, final ParamContainer container, final boolean isAction) {
        this.isAction = isAction;
        component = new UrlComponentDescription(element, container);
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

    public final UrlComponentDescription getComponent() {
        return component;
    }

    public boolean isAction() {
        return isAction;
    }

    
    public List<ParameterDescription> getFathersConstructorParameters() {
        final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
        if (father!=null) {
            parameters.addAll(father.getFathersConstructorParameters());
            parameters.addAll(father.getConstructorParameters());
        }
        return parameters;
    }
    
    public List<ParameterDescription> getConstructorParameters() {
        final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();
        for (final Entry<String, ParameterDescription> ent : component.getParameters().entrySet()) {
            if (!ent.getValue().isOptional() && ent.getValue().getRealRole() == Role.GET) {
                parameters.add(ent.getValue());
            }
        }
        return parameters;
    }
}
