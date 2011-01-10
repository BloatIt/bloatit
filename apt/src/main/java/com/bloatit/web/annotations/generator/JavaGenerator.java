package com.bloatit.web.annotations.generator;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

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

        _import.append("import com.bloatit.web.annotations.Message.Level;\n");
        _import.append("import com.bloatit.web.annotations.RequestParam.Role;\n");
        _import.append("import com.bloatit.web.utils.url.UrlParameter;\n");
        _import.append("import com.bloatit.web.utils.annotations.Loaders;\n");
        _import.append("import com.bloatit.web.utils.annotations.Loaders.*;\n");

    }

    protected abstract void generateConstructor();

    private String toCamelAttributeName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public final void addAttribute(String type, String nameString,String defaultValue, String name, Role role, Level level, String errorMsg) {
        name = toCamelAttributeName(name);
        _attributes.append("private UrlParameter<").append(type).append("> ").append(name).append(" = ")
                .append(createParameter("\"" + nameString + "\"", defaultValue, type, role, level, errorMsg));
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
        _gettersSetters.append("        return ").append(generateFrom).append(".getValue().").append(getGetterName(name)).append("();\n");
        _gettersSetters.append("    }\n");
        _gettersSetters.append("    return null;\n");
        _gettersSetters.append("}\n\n");
    }

    public final String createParameter(String nameString, String defaultValue, String type, Role role, Level level, String errorMsg) {
        // TODO find how to do this correctly
        errorMsg = errorMsg.replaceAll("[\\\"]", "\\\\\"");
        // errorMsg = errorMsg.replaceAll("([^\\\\])(\\\\)([^\\\\])",
        // "\\1\\\\\\3");
        StringBuilder sb = new StringBuilder();
        sb.append("    new UrlParameter<").append(type).append(">(").append(nameString).append(", null, ");
        sb.append("\"").append(defaultValue).append("\", ");
        sb.append(type).append(".class, ");

        switch (role) {
        case GET:
            sb.append("Role.GET, ");
            break;
        case POST:
            sb.append("Role.POST, ");
            break;
        case SESSION:
            sb.append("Role.SESSION, ");
            break;
        case PRETTY:
            sb.append("Role.PRETTY, ");
            break;
        default:
            assert false;
            break;
        }

        switch (level) {
        case ERROR:
            sb.append("Level.ERROR, ");
            break;
        case INFO:
            sb.append("Level.INFO, ");
            break;
        case WARNING:
            sb.append("Level.WARNING, ");
            break;
        default:
            assert false;
            break;
        }

        sb.append("\"").append(errorMsg).append("\"");
        sb.append(");\n");
        return sb.toString();
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
        sb.append("package com.bloatit.web.utils.url;\n");
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
