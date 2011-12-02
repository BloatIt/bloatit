package com.bloatit.framework.webprocessor.components.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bloatit.framework.webprocessor.annotations.tr;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {

    tr label();

    boolean autocomplete() default true;

}
