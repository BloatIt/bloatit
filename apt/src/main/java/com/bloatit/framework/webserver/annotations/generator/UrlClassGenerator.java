package com.bloatit.framework.webserver.annotations.generator;


public class UrlClassGenerator extends JavaGenerator {

    public UrlClassGenerator(String name, String pageName, String pageType) {
        super(name);
        _import.append("import com.bloatit.common.Log;\n");

        _classHeader.append("@SuppressWarnings(\"unused\")\n");
        _classHeader.append("public final class ").append(className).append(" extends Url  implements Cloneable {\n");
        _classHeader.append("public static String getName() { return \"").append(pageName).append("\"; }\n");
        _classHeader.append("public ").append(pageType).append(" createPage() { \n    return new ").append(pageType)
                .append("(this); }\n");

    }

    @Override
    protected void generateConstructor() {
        // Constructor with Parameters from session and from Post/Get
        _classHeader.append("public ").append(className).append("(Parameters params, Parameters session) {\n");
        _classHeader.append("    this();\n");
        _classHeader.append("    parseParameters(session, true);\n");
        _classHeader.append("    parseParameters(params, false);\n");
        _classHeader.append("}\n");

        // Constructor with required parameters
        if (_constructorParameters.length() > 0) {
            _classHeader.append("public ").append(className).append("(").append(_constructorParameters).append(") {\n");
            _classHeader.append("    this();\n");
            _classHeader.append(_constructorAssign);
            _classHeader.append("}\n");
        }

        // Constructor with 0 params
        // Must be public if the previous constructor is not existing.
        if (_constructorParameters.length() > 0) {
            _classHeader.append("private ").append(className).append("(){\n");
        } else {
            _classHeader.append("public ").append(className).append("(){\n");
        }
        _classHeader.append("    super(getName());\n");
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    try {\n");
        }
        _classHeader.append(_constructorDefaults);
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    } catch (ConversionErrorException e) {\n");
            _classHeader.append("        Log.web().fatal(e);\n");
            _classHeader.append("        assert false ;\n");
            _classHeader.append("    }\n");
        }
        _classHeader.append("}\n");
    }
}
