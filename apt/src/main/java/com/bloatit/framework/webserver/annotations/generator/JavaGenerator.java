package com.bloatit.framework.webserver.annotations.generator;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;

public abstract class JavaGenerator {

    protected final StringBuilder _import = new StringBuilder();
    protected final StringBuilder _classHeader = new StringBuilder();
    protected final String className;

    private final StringBuilder _attributes = new StringBuilder();
    private final StringBuilder _gettersSetters = new StringBuilder();
    private final StringBuilder _doRegister = new StringBuilder();
    private final StringBuilder _clone = new StringBuilder();

    protected StringBuilder _constructorParameters = new StringBuilder();
    protected StringBuilder _constructorAssign = new StringBuilder();
    protected StringBuilder _constructorDefaults = new StringBuilder();

    protected JavaGenerator(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        className = name.substring(0, 1).toUpperCase() + name.substring(1) + "Url";

        _import.append("import com.bloatit.framework.webserver.annotations.Message.Level;\n");
        _import.append("import com.bloatit.framework.webserver.annotations.RequestParam.Role;\n");
        _import.append("import com.bloatit.framework.webserver.annotations.RequestParam;\n");
        _import.append("import com.bloatit.framework.webserver.annotations.ParamConstraint;\n");
        _import.append("import com.bloatit.common.Log;\n");
        _import.append("import com.bloatit.framework.exceptions.RedirectException;\n");
        _import.append("import com.bloatit.framework.utils.*;\n");
        _import.append("import com.bloatit.framework.webserver.url.*;\n");
        _import.append("import com.bloatit.framework.webserver.url.Loaders.*;\n");

    }

    protected abstract void generateConstructor();

    private String toCamelAttributeName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public final void addAttribute(String type,
                                   String nameString,
                                   String defaultValue,
                                   String name,
                                   Role role,
                                   Level level,
                                   String malFormedMsg,
                                   ParamConstraint constraints) {
        name = toCamelAttributeName(name);

        String createParameter = createParameter("\"" + nameString + "\"", defaultValue, type, role, level, malFormedMsg, constraints);
        _attributes.append("private UrlParameter<").append(type).append("> ").append(name).append(" = ").append(createParameter);
        _clone.append("    other.").append(name).append(" = ").append("this.").append(name).append(".clone();\n");
    }

    public final void addConstructorParameter(String type, String attributeName) {
        attributeName = toCamelAttributeName(attributeName);
        if (_constructorParameters.length() > 0) {
            _constructorParameters.append(", ");
        }
        _constructorParameters.append(type).append(" ").append(attributeName);
        _constructorAssign.append("        this.").append(attributeName).append(".setValue(").append(attributeName).append(");\n");
    }

    public final void addDefaultParameter(String attributeName, String className, String defaultValue) {
        attributeName = toCamelAttributeName(attributeName);
        _constructorDefaults.append("        this.").append(attributeName).append(".setValue(").append("Loaders.fromStr(").append(className)
                .append(".class, \"").append(defaultValue).append("\"));\n");
    }

    public void addGetterSetter(String type, String name) {
        _gettersSetters.append("public ").append(type).append(" ").append(getGetterName(name)).append("(){ \n");
        _gettersSetters.append("    return this.").append(name).append(".getValue();\n");
        _gettersSetters.append("}\n\n");

        _gettersSetters.append("public UrlParameter<").append(type).append("> ").append(getGetterName(name)).append("Parameter(){ \n");
        _gettersSetters.append("    return this.").append(name).append(";\n");
        _gettersSetters.append("}\n\n");

        _gettersSetters.append("public void ").append(getSetterName(name)).append("(").append(type).append(" arg){ \n");
        _gettersSetters.append("    this.").append(name).append(".setValue(arg);\n");
        _gettersSetters.append("}\n\n");
    }

    public void addAutoGeneratingGetter(String type, String name, String generateFrom) {
        _gettersSetters.append("public ").append(type).append(" ").append(getGetterName(name)).append("(){ \n");
        _gettersSetters.append("    if (").append(generateFrom).append(".getValue() != null) {\n");
        _gettersSetters.append("    try{\n");
        _gettersSetters.append("        return ").append(generateFrom).append(".getValue().").append(getGetterName(name)).append("();\n");
        _gettersSetters.append("    } catch (Exception e) {\n");
        _gettersSetters.append("    // do nothing.\n");
        _gettersSetters.append("    }\n");
        _gettersSetters.append("    }\n");
        _gettersSetters.append("    return null;\n");
        _gettersSetters.append("}\n\n");
    }

    // nameString must be already escaped.
    public final String createParameter(String nameString,
                                        String defaultValue,
                                        String type,
                                        Role role,
                                        Level level,
                                        String malFormedMsg,
                                        ParamConstraint constraints) {

        StringBuilder sb = new StringBuilder();
        sb.append(" new UrlParameter<").append(type).append(">(");
        sb.append("null, ");

        sb.append("new UrlParameterDescription<").append(type).append(">(");
        sb.append(nameString).append(", ").append(type).append(".class, ");
        addRole(role, sb);
        if (defaultValue.equals(RequestParam.DEFAULT_DEFAULT_VALUE)){
            sb.append(", ").append("RequestParam.DEFAULT_DEFAULT_VALUE").append(", ");
        }else{
            sb.append(", \"").append(defaultValue).append("\", ");
        }
        if (malFormedMsg.equals(RequestParam.DEFAULT_ERROR_MSG)){
            sb.append("RequestParam.DEFAULT_ERROR_MSG").append(", ");
        }else{
            sb.append("\"").append(malFormedMsg.replaceAll("[\\\"]", "\\\\\"")).append("\", ");
        }
        addLevel(level, sb);
        sb.append("), ");

        sb.append("new UrlParameterConstraints<").append(type).append(">(");
        if (constraints != null) {
            sb.append(constraints.min().equals(ParamConstraint.DEFAULT_MIN_STR) ? ParamConstraint.DEFAULT_MIN : constraints.min()).append(", ");
            sb.append(constraints.max().equals(ParamConstraint.DEFAULT_MAX_STR) ? ParamConstraint.DEFAULT_MAX : constraints.max()).append(", ");
            sb.append(constraints.optional()).append(", ");
            sb.append(constraints.precision()).append(", ");
            sb.append(constraints.length()).append(", ");
            appendErrorMsg(constraints.minErrorMsg().value(), sb).append(", ");
            appendErrorMsg(constraints.maxErrorMsg().value(), sb).append(", ");
            appendErrorMsg(constraints.optionalErrorMsg().value(), sb).append(", ");
            appendErrorMsg(constraints.precisionErrorMsg().value(), sb).append(", ");
            appendErrorMsg(constraints.LengthErrorMsg().value(), sb);
        }
        sb.append(")");

        sb.append(");\n");
        return sb.toString();
    }

    private StringBuilder appendErrorMsg(String msg, StringBuilder sb) {
        if (msg.equals(ParamConstraint.DEFAULT_ERROR_MSG)){
            return sb.append("ParamConstraint.DEFAULT_ERROR_MSG");
        }
        return sb.append("\"").append(msg.replaceAll("[\\\"]", "\\\\\"")).append("\"");
    }

    private void addLevel(Level level, StringBuilder sb) {
        switch (level) {
        case ERROR:
            sb.append("Level.ERROR");
            break;
        case INFO:
            sb.append("Level.INFO");
            break;
        case WARNING:
            sb.append("Level.WARNING");
            break;
        default:
            assert false;
            break;
        }
    }

    private void addRole(Role role, StringBuilder sb) {
        switch (role) {
        case GET:
            sb.append("Role.GET");
            break;
        case POST:
            sb.append("Role.POST");
            break;
        case SESSION:
            sb.append("Role.SESSION");
            break;
        case PRETTY:
            sb.append("Role.PRETTY");
            break;
        default:
            assert false;
            break;
        }
    }

    private String getGetterName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getSetterName(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public final void registerComponent(String name) {
        _doRegister.append("    register(").append(getComponentName(name)).append(");\n");
    }

    public final void addComponentAndGetterSetter(String type, String name) {
        type = getComponentType(type);
        name = getComponentName(name);

        _attributes.append("private ").append(type).append(" ").append(name).append(" = new ").append(type).append("();\n");
        _clone.append("    other.").append(name).append(" = ").append("this.").append(name).append(".clone();\n");

        _gettersSetters.append("public ").append(type).append(" ").append(getGetterName(name)).append("(){ \n");
        _gettersSetters.append("    return this.").append(name).append(";\n");
        _gettersSetters.append("}\n\n");

        _gettersSetters.append("public void ").append(getSetterName(name)).append("(").append(type).append(" arg){ \n");
        _gettersSetters.append("    this.").append(name).append(" = arg;\n");
        _gettersSetters.append("}\n\n");

    }

    private final String getComponentName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1) + "Url";
    }

    private final String getComponentType(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1) + "Url";
    }

    public final String generate() {

        generateConstructor();

        StringBuilder sb = new StringBuilder();
        sb.append("package com.bloatit.web.url;\n");
        sb.append("\n");
        sb.append(_import);
        sb.append("\n");
        sb.append(_classHeader);

        sb.append(_attributes);
        sb.append("\n");
        sb.append(_gettersSetters);
        sb.append("\n");

        sb.append("@Override \nprotected void doRegister() { \n");
        sb.append(_doRegister);
        sb.append("}\n");
        sb.append("\n");

        sb.append("@Override \npublic ").append(className).append(" clone() { \n");
        sb.append("    ").append(className).append(" other = new ").append(className).append("();\n");
        sb.append(_clone);
        sb.append("    return other;\n");
        sb.append("}\n");

        sb.append("}\n");

        return sb.toString();
    }

    public final String getClassName() {
        return className;
    }

    public void registerAttribute(String attributeName) {
        _doRegister.append("    register(").append(attributeName).append(");\n");
    }
}
