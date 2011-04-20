package com.bloatit.framework.webprocessor.annotations.generator;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.TypeKindVisitor6;

public class Utils {

    public static String getStr(final String str) {
        if (str == null) {
            return "null";
        }
        return "\"" + str + "\"";
    }

    public static String toCamelCase(final String str, final boolean firstUpper) {
        // TODO make me work correctly
        if (firstUpper) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String getDeclaredName(final Element elment) {
        return elment.getEnclosingElement().asType().accept(new GetDeclaredNameVisitor(), null);
    }

    private static class GetDeclaredNameVisitor extends TypeKindVisitor6<String, Integer> {
        public GetDeclaredNameVisitor() {
            super();
        }

        @Override
        public String visitDeclared(final DeclaredType t, final Integer p) {
            return t.asElement().getSimpleName().toString();
        }
    }
}
