package com.bloatit.framework.rest;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class RestResource {
    private final Object underlying;
    private final Class<?>[] classes;
    private final String request;

    public RestResource(Object underlying, String request, Class<?>... classes) {
        this.underlying = underlying;
        this.classes = classes;
        this.request = request;
    }

    public final String getXmlString() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(classes);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty("com.sun.xml.bind.xmlDeclaration", false);

        StringWriter sw = new StringWriter();
        m.marshal(underlying, sw);
        return sw.toString();
    }

    public Object getUnderlying() {
        return underlying;
    }

    public String getRequest() {
        return request;
    }
}
