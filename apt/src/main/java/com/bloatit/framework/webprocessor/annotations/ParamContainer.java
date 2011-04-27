package com.bloatit.framework.webprocessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ParamContainer {

    enum Protocol {
        AUTO,
        HTTPS,
        HTTP
    }

    String value();

    Protocol protocol() default Protocol.AUTO;

    boolean isComponent() default false;
}
