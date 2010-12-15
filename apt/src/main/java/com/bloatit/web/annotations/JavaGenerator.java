package com.bloatit.web.annotations;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class JavaGenerator {

    protected final StringBuilder _import = new StringBuilder();
    protected final StringBuilder _classHeader = new StringBuilder();
    protected final String className;

    private StringBuilder _attributes = new StringBuilder();
    private StringBuilder _gettersSetters = new StringBuilder();
    private StringBuilder _doRegister = new StringBuilder();
    private StringBuilder _clone = new StringBuilder();

    protected JavaGenerator(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        className = name.substring(0, 1).toUpperCase() + name.substring(1) + "Url";

        _import.append("import com.bloatit.web.annotations.Message.Level;\n");
        _import.append("import com.bloatit.web.annotations.RequestParam.Role;\n");

    }

    public final void addAttribute(String type, String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        _attributes.append("private ").append(type).append(" ").append(name).append(";\n");
        _clone.append("    other.").append(name).append(" = ").append("this.").append(name).append(";\n");
    }

    public void addGetterSetter(String type, String name) {
        _gettersSetters.append("public ").append(type).append(" ").append(getGetterName(name)).append("{ \n");
        _gettersSetters.append("    return this.").append(name).append(";\n");
        _gettersSetters.append("}\n\n");

        _gettersSetters.append("public void ").append(getSetterName(name)).append("(").append(type).append(" arg0){ \n");
        _gettersSetters.append("    this.").append(name).append(" = arg0;\n");
        _gettersSetters.append("}\n\n");
    }

    public void addAutoGeneratingGetter(String type, String name, String generateFrom) {
        _gettersSetters.append("public ").append(type).append(" ").append(getGetterName(name)).append("{ \n");
        _gettersSetters.append("    if (").append(generateFrom).append(" != null) {\n");
        _gettersSetters.append("        return ").append(generateFrom).append(".").append(getGetterName(name)).append(";\n");
        _gettersSetters.append("    } else {\n");
        _gettersSetters.append("        return null;\n");
        _gettersSetters.append("    }\n");
        _gettersSetters.append("}\n\n");
    }

    public final void registerAttribute(String name, String nameString, String type, Role role, Level level, String errorMsg) {
        name = getGetterName(name);
        // TODO find how to do this correctly
        errorMsg = errorMsg.replaceAll("[\\\"]", "\\\\\"");
        // errorMsg = errorMsg.replaceAll("([^\\\\])(\\\\)([^\\\\])",
        // "\\1\\\\\\3");
        _doRegister.append("    register(new Parameter(\"").append(nameString).append("\"").append(", ");
        _doRegister.append(name).append(", ");
        _doRegister.append(type).append(".class, ");

        switch (role) {
        case GET:
            _doRegister.append("Role.GET, ");
            break;
        case POST:
            _doRegister.append("Role.POST, ");
            break;
        case PRETTY:
            _doRegister.append("Role.PRETTY, ");
            break;
        }

        switch (level) {
        case ERROR:
            _doRegister.append("Level.ERROR, ");
            break;
        case INFO:
            _doRegister.append("Level.INFO, ");
            break;
        case WARNING:
            _doRegister.append("Level.WARNING, ");
            break;
        }

        _doRegister.append("\"").append(errorMsg).append("\"");
        _doRegister.append("));\n");
    }

    private String getGetterName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1) + "()";
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
        _clone.append("    other.").append(name).append(" = ").append("this.").append(name).append(";\n");
        addGetterSetter(type, name);
    }

    private final String getComponentName(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1) + "Url";
    }

    private final String getComponentType(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1) + "Url";
    }

    public final String generate() {

        _classHeader.append("public ").append(className).append("(Parameters params) {\n");
        _classHeader.append("    this();\n");
        _classHeader.append("    parseParameters(params);\n");
        _classHeader.append("}\n");

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

        sb.append("public ").append(className).append(" clone() { \n");
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
}