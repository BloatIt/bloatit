package com.bloatit.framework.restprocessor;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Small class used to encapsulate a rest resource, and Marshalling operations
 */
public class RestResource {
    private final Object underlying;
    private final Class<?>[] classes;
    private final String request;
    private static JAXBContext context;// JAXBContext IS THREAD SAFE

    /**
     * Creates a rest resource
     *
     * @param underlying the object returned by the call to the rest method
     * @param request the string representation of the reqyest
     * @param classes the list of classes available to convert
     *            <code>underlying</code> to XML
     */
    public RestResource(Object underlying, String request, Class<?>... classes) {
        this.underlying = underlying;
        this.classes = classes;
        this.request = request;
    }

    /**
     * Converts <code>underlying</code> to XML
     *
     * @return the XML string obtained from converting <code>underlying</code>
     *         to XML
     * @throws JAXBException when an error occurs during conversion
     */
    public final String getXmlString() throws JAXBException {
        if (context == null) {
            context = JAXBContext.newInstance(classes);
        }
        // Marshaller is not thread-safe, and creating one can be expensive. If
        // needed add pooling.
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty("com.sun.xml.bind.xmlDeclaration", false);

        StringWriter sw = new StringWriter();
        m.marshal(underlying, sw);
        return sw.toString();
    }

    /**
     * @return the underlying object
     */
    public Object getUnderlying() {
        return underlying;
    }

    /**
     * @return the String representation of the ReST request
     */
    public String getRequest() {
        return request;
    }

    /**
     * @return <code>true</code> when <code>underlying</code> is null,
     *         <code>false</code> otherwise
     */
    public boolean isNull() {
        return underlying == null;
    }
}
