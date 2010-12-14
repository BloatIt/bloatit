package com.bloatit.web.annotations;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class UrlClassGenerator {

    private StringBuilder _import = new StringBuilder();
    private StringBuilder _classHeader = new StringBuilder();
    private StringBuilder _attributes = new StringBuilder();
    private StringBuilder _gettersSetters = new StringBuilder();
    private StringBuilder _doRegister = new StringBuilder();

    public UrlClassGenerator(String name) {
        super();
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        String className = name.substring(0, 1).toUpperCase() + name.substring(1) + "Url";  

        _import.append("import java.util.Map;\n");
        _import.append("import com.bloatit.web.annotations.Message.Level;\n");
        _import.append("import com.bloatit.web.annotations.RequestParam.Role;\n");
        _import.append("import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;\n");

        _classHeader.append("public class ").append(className).append(" extends Url {\n");
        _classHeader.append("public ").append(className).append("() {\n");
        _classHeader.append("super(\"").append(name).append("\"); }\n");
        
        _classHeader.append("public ").append(className).append("(Map<String, String> params) {\n");
        _classHeader.append("super(\"").append(name).append("\"); \n");
        _classHeader.append("parseParameterMap(params);\n}\n");
    }

    public void addAttribute(String type, String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        _attributes.append("private ").append(type).append(" ").append(name).append(";");
        
        _gettersSetters.append("public ").append(type).append(" get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("(){ ");
        _gettersSetters.append("return this.").append(name).append(";\n}");
        
        _gettersSetters.append("public void set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).append("(").append(type).append(" arg0){ ");
        _gettersSetters.append("this.").append(name).append(" = arg0;\n}");
    }
    
    public void registerAttribute(String name, Role role, Level level, String errorMsg){
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        // TODO find how to do this correctly
        errorMsg = errorMsg.replaceAll("[\\\"]", "\\\\\"");
//        errorMsg = errorMsg.replaceAll("([^\\\\])(\\\\)([^\\\\])", "\\1\\\\\\3");
        _doRegister.append("register(new Parameter(messages, ").append("\"").append(name).append("\"").append(", ");
        _doRegister.append(name).append(", ");

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
        _doRegister.append("));");
//        register(new Parameter(messages, "plop", plop, Role.GET, Level.ERROR, "plop"));
    }
    
    public void registerComponent(String name){
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        _doRegister.append("register(").append(name).append(");");
    }

    public void addComponent(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        String type = name.substring(0, 1).toUpperCase() + name.substring(1); 
        addAttribute(type, name);
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.bloatit.web.utils.url;\n");
        sb.append(_import);
        sb.append(_classHeader);
        
        sb.append(_attributes);
        sb.append(_gettersSetters);
        
        sb.append("@Override protected void doRegister(Messages messages) { \n");
        sb.append(_doRegister);
        sb.append("}");
        
        sb.append("}");
        
        return sb.toString();
    }

}
