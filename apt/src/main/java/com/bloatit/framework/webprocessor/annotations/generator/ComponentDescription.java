package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public class ComponentDescription extends ClassDescription {

    private final String className;
    private final String attributeName;
    private final boolean isComponent;
    private final ParamContainer.Protocol protocol;
    private final String codeName;
    private final List<ComponentDescription> children = new ArrayList<ComponentDescription>();

    private final List<ParameterDescription> parameters = new ArrayList<ParameterDescription>();

    public ComponentDescription(final Element element, final ParamContainer container, final String attributeName) {
        className = element.getSimpleName().toString() + "UrlComponent";
        isComponent = container.isComponent();
        codeName = container.value();
        protocol = container.protocol();
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

    public final String getCodeName() {
        return codeName;
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
            if ((param.getRealRole() == Role.GET || param.getRealRole() == Role.PAGENAME) && !param.isOptional()) {
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
        // TODO maybe a little optimization here ?
        Collections.sort(parameters);
    }

    public void addSubComponent(final ComponentDescription child) {
        children.add(child);
    }

    public List<ComponentDescription> getSubComponents() {
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

    public ParamContainer.Protocol getProtocol() {
        return protocol;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((codeName == null) ? 0 : codeName.hashCode());
        result = prime * result + (isComponent ? 1231 : 1237);
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComponentDescription other = (ComponentDescription) obj;
        if (attributeName == null) {
            if (other.attributeName != null)
                return false;
        } else if (!attributeName.equals(other.attributeName))
            return false;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (codeName == null) {
            if (other.codeName != null)
                return false;
        } else if (!codeName.equals(other.codeName))
            return false;
        if (isComponent != other.isComponent)
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        if (protocol != other.protocol)
            return false;
        return true;
    }

}
