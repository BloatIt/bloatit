
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 							This element is the request for the
 * 							getMassTraitmentDetails method
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
 *         &lt;element name="massTraitmentID" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "massTraitmentID"
})
@XmlRootElement(name = "getMassTraitmentDetailsRequest")
public class GetMassTraitmentDetailsRequest {

    @XmlElement(required = true)
    protected String massTraitmentID;

    /**
     * Gets the value of the massTraitmentID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMassTraitmentID() {
        return massTraitmentID;
    }

    /**
     * Sets the value of the massTraitmentID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMassTraitmentID(String value) {
        this.massTraitmentID = value;
    }

}
