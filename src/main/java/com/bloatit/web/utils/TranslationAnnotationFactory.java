package com.bloatit.web.utils;

import java.util.Collection;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class TranslationAnnotationFactory implements AnnotationProcessorFactory {

    @Override
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> arg0, AnnotationProcessorEnvironment arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> supportedOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    
    
}
