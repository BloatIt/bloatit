
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.CaptureAuthorizationList;


/**
 * 
 * 							This element is the request for the
 * 							doMassCapture method
 * 						
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="captureAuthorizationList" type="{http://obj.ws.payline.experian.com}captureAuthorizationList"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "captureAuthorizationList",
    "comment"
})
@XmlRootElement(name = "doMassCaptureRequest")
public class DoMassCaptureRequest {

    @XmlElement(required = true, nillable = true)
    protected CaptureAuthorizationList captureAuthorizationList;
    @XmlElement(required = true, nillable = true)
    protected String comment;

    /**
     * Gets the value of the captureAuthorizationList property.
     * 
     * @return
     *     possible object is
     *     {@link CaptureAuthorizationList }
     *     
     */
    public CaptureAuthorizationList getCaptureAuthorizationList() {
        return captureAuthorizationList;
    }

    /**
     * Sets the value of the captureAuthorizationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CaptureAuthorizationList }
     *     
     */
    public void setCaptureAuthorizationList(CaptureAuthorizationList value) {
        this.captureAuthorizationList = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
