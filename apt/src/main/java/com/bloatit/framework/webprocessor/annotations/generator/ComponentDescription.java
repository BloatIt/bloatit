package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class ComponentDescription extends ClassDescription {

    private final String className;
    private final String attributeName;
    private final boolean isComponent;
    private final String codeName;
    private final Set<ComponentDescription> children = new HashSet<ComponentDescription>();

    private final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

    public ComponentDescription(final Element element, final ParamContainer container, final String attributeName) {
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

    public final List<ParameterDescription> getParameters() {
        return parameters;
    }

    public boolean hasUrlParameter() {
        return getUrlParameters().size() > 0 || getAllUrlParameters().size() > 0;
    }

    public final List<ParameterDescription> getUrlParameters() {
        final List<ParameterDescription> urlParameters = new ArrayList<ParameterDescription>();
        for (final ParameterDescription param : parameters) {
            if (param.getRealRole() == Role.GET && !param.isOptional()) {
                urlParameters.add(param);
            }
        }
        return urlParameters;
    }

    /**
     * @return All the parameters of this component + the parameters of all the
     *         subComponents.
     */
    public final List<ParameterDescription> getAllUrlParameters() {
        final List<ParameterDescription> urlParameters = new ArrayList<ParameterDescription>();
        urlParameters.addAll(getUrlParameters());
        for (final ComponentDescription child : children) {
            urlParameters.addAll(child.getUrlParameters());
        }
        return urlParameters;
    }

    public void addParameter(final ParameterDescription description) {
        parameters.add(description);
    }

    public void addSubComponent(final ComponentDescription child) {
        children.add(child);
    }

    public Set<ComponentDescription> getSubComponents() {
        return children;
    }

    public List<ParameterDescription> getParameterGeneratedFromMe(final ParameterDescription me) {
        final List<ParameterDescription> returnParams = new ArrayList<ParameterDescription>();
        for (final ParameterDescription param : parameters) {
            if (param.getGenerateFrom() != null && param.getGenerateFrom().equals(me.getAttributeName())) {
                returnParams.add(param);
            }
        }
        return returnParams;
    }

}
