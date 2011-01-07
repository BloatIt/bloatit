
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.Card;


/**
 * 
 * 							This element is the request for the
 * 							doAuthentication method
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
 *         &lt;element name="contractNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pares" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="md" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="card" type="{http://obj.ws.payline.experian.com}card"/>
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
    "contractNumber",
    "pares",
    "md",
    "card"
})
@XmlRootElement(name = "verifyAuthenticationRequest")
public class VerifyAuthenticationRequest {

    @XmlElement(required = true)
    protected String contractNumber;
    @XmlElement(required = true)
    protected String pares;
    @XmlElement(required = true, nillable = true)
    protected String md;
    @XmlElement(required = true)
    protected Card card;

    /**
     * Gets the value of the contractNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * Sets the value of the contractNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractNumber(String value) {
        this.contractNumber = value;
    }

    /**
     * Gets the value of the pares property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPares() {
        return pares;
    }

    /**
     * Sets the value of the pares property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPares(String value) {
        this.pares = value;
    }

    /**
     * Gets the value of the md property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMd() {
        return md;
    }

    /**
     * Sets the value of the md property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMd(String value) {
        this.md = value;
    }

    /**
     * Gets the value of the card property.
     * 
     * @return
     *     possible object is
     *     {@link Card }
     *     
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the value of the card property.
     * 
     * @param value
     *     allowed object is
     *     {@link Card }
     *     
     */
    public void setCard(Card value) {
        this.card = value;
    }

}
