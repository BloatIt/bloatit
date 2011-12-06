//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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
    protected RestResource(final Object underlying, final String request, final Class<?>... classes) {
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
        final Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.setProperty("com.sun.xml.bind.xmlDeclaration", false);

        final StringWriter sw = new StringWriter();
        if(underlying instanceof Boolean) {
            sw.append(((Boolean) underlying).toString());
        } else {
            m.marshal(underlying, sw);
        }
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
