package com.bloatit.web.annotations;

public class UrlComponentClassGenerator extends JavaGenerator {

    public UrlComponentClassGenerator(String name) {
        super(name);
        _classHeader.append("public class ").append(className).append(" extends UrlComponent {\n");
        _classHeader.append("public ").append(className).append("() {\n");
        _classHeader.append("    super(); \n}\n");
        
    }

}
