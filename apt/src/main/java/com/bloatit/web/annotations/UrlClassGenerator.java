package com.bloatit.web.annotations;

public class UrlClassGenerator extends JavaGenerator {

    public UrlClassGenerator(String name) {
        super(name);
        _classHeader.append("public class ").append(className).append(" extends Url {\n");
        _classHeader.append("public ").append(className).append("() {\n");
        _classHeader.append("    super(\"").append(name).append("\"); \n}\n");
    }

}
