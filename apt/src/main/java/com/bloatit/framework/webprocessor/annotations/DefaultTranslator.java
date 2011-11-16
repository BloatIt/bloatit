package com.bloatit.framework.webprocessor.annotations;

public class DefaultTranslator implements Translator {

    @Override
    public String tr(String message) {
        return message;
    }

}
