package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Generator {
    public static enum Modifier {
        PRIVATE("private"), PROTECTED("protected"), DEFAULT(""), PUBLIC("public");

        private final String name;

        private Modifier(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    };

    public static class Method {
        private class Parameter {
            public final String type;
            public final String name;

            Parameter(final String type, final String name) {
                super();
                this.type = type;
                this.name = name;
            }
        }

        private final String name;
        private final String type;
        private Modifier modifier = Modifier.PUBLIC;
        private final List<Parameter> parameters = new ArrayList<Generator.Method.Parameter>();
        private final Set<String> annotations = new HashSet<String>();
        private StringBuilder content = new StringBuilder();
        private String staticFinal = "";

        public Method(final String type, final String name) {
            this.type = type;
            this.name = name;
        }

        public void addParameter(final String type, final String name) {
            parameters.add(new Parameter(type, name));
        }

        public void addLine(final Object content) {
            this.content.append("        ").append(content.toString()).append("\n");
        }

        public void setOverride() {
            this.annotations.add("@Override");
        }

        public void setStaticFinal(final String stf) {
            this.staticFinal = stf;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (String ann : annotations) {
                sb.append("    " + ann + "\n");
            }
            sb.append("    ").append(modifier.getName()).append(" ");
            if (!staticFinal.isEmpty()) {
                sb.append(staticFinal).append(" ");
            }
            sb.append(type).append(" ").append(name).append("(");
            if (content.toString().isEmpty()) {
                addLine("// Generator: Empty block !");
            }
            for (int i = 0; i < parameters.size(); ++i) {
                final Parameter param = parameters.get(i);
                sb.append(param.type).append(" ").append(param.name);
                if (i != parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") {\n");
            sb.append(content);
            sb.append("    }\n");
            return sb.toString();
        }

        public void setModifier(final Modifier modifier) {
            this.modifier = modifier;
        }

        public void addAnnotation(String string) {
            annotations.add(string);
        }
    }

    public static class MethodCall {

        private final String name;
        private final List<String> parameters = new ArrayList<String>();

        public MethodCall(final String name) {
            this.name = name;
        }

        public void addParameter(final String name) {
            parameters.add(name);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(name).append("(");
            for (int i = 0; i < parameters.size(); ++i) {
                final String param = parameters.get(i);
                sb.append(param);
                if (i != parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }

    public static class Attribute {
        private final String type;
        private final String name;
        private String staticEquals;
        private boolean generateGetter;
        private boolean generateSetter;
        private boolean isStatic;
        private Set<String> annotations = new HashSet<String>();

        public Attribute(final String type, final String name) {
            this.type = type;
            this.name = name;
            staticEquals = "";
            generateGetter = false;
            generateSetter = false;
            isStatic = false;
        }

        public void generateGetter() {
            generateGetter = true;
        }

        public void generateSetter() {
            generateSetter = true;
        }

        public void setStaticEquals(final Object staticEquals) {
            this.staticEquals = staticEquals.toString();
        }

        public void setStatic(final boolean isStatic) {
            this.isStatic = isStatic;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            for (String anno : annotations) {
                sb.append(anno + "\n");
            }
            sb.append("private ");
            if (isStatic) {
                sb.append("static ");
            }
            sb.append(type).append(" ").append(name);
            if (!staticEquals.isEmpty()) {
                sb.append(" = ").append(staticEquals);
            }
            sb.append(";\n");

            if (generateGetter) {
                final Method getter = new Method(type, "get" + Utils.firstCharUpper(name));
                if (isStatic) {
                    getter.setStaticFinal("static");
                }
                getter.addLine("return this." + name + ";");
                sb.append(getter);
            }
            if (generateSetter) {
                final Method setter = new Method(type, "set" + Utils.firstCharUpper(name));
                if (isStatic) {
                    setter.setStaticFinal("static");
                }
                setter.addParameter(type, "other");
                setter.addLine("this." + name + " = other;\n");
                sb.append(setter);
            }
            return sb.toString();
        }

        public void addAnnotation(String string) {
            annotations.add(string);
        }
    }

    public static class Clazz {

        private final String className;
        private final String packageName;
        private String extendsClass;
        private final List<String> implementsClass = new ArrayList<String>();
        private final List<Attribute> attributes = new ArrayList<Generator.Attribute>();
        private final List<Method> methods = new ArrayList<Generator.Method>();
        private final StringBuilder imports = new StringBuilder();

        public Clazz(final String className, final String packageName) {
            this.className = className;
            this.packageName = packageName;
        }

        public void addImplements(final String interfaceName) {
            implementsClass.add(interfaceName);
        }

        public void setExtends(final String clazz) {
            extendsClass = clazz;
        }

        public Method addMethod(final String type, final String name) {
            final Method method = new Method(type, name);
            methods.add(method);
            return method;
        }

        public Method addConstructor() {
            final Method method = new Method("", className);
            methods.add(method);
            return method;
        }

        public void addImport(final String importLine) {
            imports.append("import ").append(importLine).append(";\n");
        }

        public Attribute addAttribute(final String type, final String name) {
            return addAttribute(type, name, null, null);
        }

        public Attribute addAttribute(final String type, final String name, final String getterName, final String setterName) {
            final Attribute attribute = new Attribute(type, name);
            attributes.add(attribute);

            if (getterName != null) {
                final Method getter = new Method(type, getterName);
                getter.addLine("return this." + name + ";\n");
                methods.add(getter);
            }
            if (setterName != null) {
                final Method setter = new Method("void", setterName);
                setter.addParameter(type, "other");
                setter.addLine("this." + name + " = other;\n");
                methods.add(setter);
            }

            return attribute;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("package ").append(packageName).append(";\n");
            sb.append(imports);
            sb.append("\n");

            sb.append("public class ").append(className);
            if (extendsClass != null) {
                sb.append(" extends ").append(extendsClass);
            }

            for (int i = 0; i < implementsClass.size(); ++i) {
                if (i == 0) {
                    sb.append(" implements ");
                }
                sb.append(implementsClass.get(i));
                if (i != implementsClass.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append(" {\n");

            for (final Attribute attribute : attributes) {
                sb.append(attribute);
                sb.append("\n");
            }

            for (final Method method : methods) {
                sb.append(method);
                sb.append("\n");
            }

            sb.append(" }\n");
            return sb.toString();
        }

        public String getName() {
            return className;
        }

        public String getQualifiedName() {
            return packageName + "." + className;
        }
    }
}
