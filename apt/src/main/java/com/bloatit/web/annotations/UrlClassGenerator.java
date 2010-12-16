package com.bloatit.web.annotations;

public class UrlClassGenerator extends JavaGenerator {

    public UrlClassGenerator(String name) {
        super(name);
        _classHeader.append("@SuppressWarnings(\"unused\")\n");
        _classHeader.append("public class ").append(className).append(" extends Url {\n");
        _classHeader.append("public static String getName() { return \"").append(name).append("\"; }\n");

    }

    @Override
    protected void generateConstructor() {

        _classHeader.append("public ").append(className).append("(Parameters params) {\n");
        _classHeader.append("    super(getName());\n");
        _classHeader.append("    parseParameters(params);\n");
        _classHeader.append("}\n");

        _classHeader.append("public ").append(className).append("(").append(_constructorParameters).append(") {\n");
        _classHeader.append("    super(getName());\n");
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    try {\n");
        }
        _classHeader.append(_constructorDefaults);
        if (_constructorDefaults.length() > 0) {
            _classHeader.append("    } catch (ConversionErrorException e) {\n");
            _classHeader.append("        e.printStackTrace();\n");
            _classHeader.append("        assert false ;\n");
            _classHeader.append("    }\n");
        }
        _classHeader.append(_constructorAssign);
        _classHeader.append("}\n");

        if (_constructorParameters.length() > 0) {
            _classHeader.append("private ").append(className).append("(){\n");
            _classHeader.append("    super(getName());\n");
            _classHeader.append("}\n");
        }

    }

}
