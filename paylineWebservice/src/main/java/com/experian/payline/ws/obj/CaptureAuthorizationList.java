
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						An array of authorization to capture
 * 					
 * 
 * <p>Java class for captureAuthorizationList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="captureAuthorizationList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="capture" type="{http://obj.ws.payline.experian.com}capture" maxOccurs="5000"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "captureAuthorizationList", propOrder = {
    "capture"
})
public class CaptureAuthorizationList {

    @XmlElement(required = true)
    protected List<Capture> capture;

    /**
     * Gets the value of the capture property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capture property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCapture().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Capture }
     * 
     * 
     */
    public List<Capture> getCapture() {
        if (capture == null) {
            capture = new ArrayList<Capture>();
        }
        return this.capture;
    }

}
