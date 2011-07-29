
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains information e-ticket
 * 					
 * 
 * <p>Java class for ticketSend complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ticketSend">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="toBuyer" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="toMerchant" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ticketSend", propOrder = {
    "toBuyer",
    "toMerchant"
})
public class TicketSend {

    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean toBuyer;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean toMerchant;

    /**
     * Gets the value of the toBuyer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isToBuyer() {
        return toBuyer;
    }

    /**
     * Sets the value of the toBuyer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setToBuyer(Boolean value) {
        this.toBuyer = value;
    }

    /**
     * Gets the value of the toMerchant property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isToMerchant() {
        return toMerchant;
    }

    /**
     * Sets the value of the toMerchant property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setToMerchant(Boolean value) {
        this.toMerchant = value;
    }

}
