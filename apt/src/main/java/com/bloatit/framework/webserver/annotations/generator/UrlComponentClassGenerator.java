package com.bloatit.framework.webserver.annotations.generator;

public class UrlComponentClassGenerator extends JavaGenerator {

    public UrlComponentClassGenerator(final String name, final String pageName) {
        super(name, pageName);
        _classHeader.append("@SuppressWarnings(\"unused\")\n");
        _classHeader.append("public final class ").append(componentClassName).append(" extends UrlComponent {\n");
    }

    @Override
    protected void generateConstructor() {
        // Constructor with Parameters from session and from Post/Get
        _classHeader.append("public ").append(componentClassName).append("(Parameters params, SessionParameters session) {\n");
        _classHeader.append("    this();\n");
        _classHeader.append("    parseSessionParameters(session);\n");
        _classHeader.append("    parseParameters(params);\n");
        _classHeader.append("}\n");

        // Constructor with required parameters
        if (_constructorParameters.length() > 0) {
            _classHeader.append("public ").append(componentClassName).append("(").append(_constructorParameters).append(") {\n");
            _classHeader.append("    this();\n");
            _classHeader.append(_constructorAssign);
            _classHeader.append("}\n");

            _urlClassConstructor.append("public ").append(className).append("(").append(_constructorParameters).append("){\n");
            _urlClassConstructor.append("     super(getName());\n");
            _urlClassConstructor.append("     component =  new ").append(componentClassName).append("(").append(_constructorNames).append(");\n");
            _urlClassConstructor.append("}\n");
        }

        // Constructor with 0 params
        // Must be public if the previous constructor is not existing.
        if (_constructorParameters.length() > 0) {
            _classHeader.append("private ").append(componentClassName).append("(){\n");
        } else {
            _classHeader.append("public ").append(componentClassName).append("(){\n");
        }
        _classHeader.append("    super();\n");
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    try {\n");
        }
        _classHeader.append(_constructorDefaults);
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    } catch (ConversionErrorException e) {\n");
            _classHeader.append("        Log.web().fatal(\"conversion error in url\", e);\n");
            _classHeader.append("        assert false ;\n");
            _classHeader.append("    }\n");
        }
        _classHeader.append("}\n");
    }
}
