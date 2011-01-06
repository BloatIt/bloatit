
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains IBAN information
 * 					
 * 
 * <p>Java class for iban complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="iban">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="checkKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BBAN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BIC" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iban", propOrder = {
    "countryCode",
    "checkKey",
    "bban",
    "bic"
})
public class Iban {

    @XmlElement(name = "CountryCode", required = true, nillable = true)
    protected String countryCode;
    @XmlElement(required = true, nillable = true)
    protected String checkKey;
    @XmlElement(name = "BBAN", required = true, nillable = true)
    protected String bban;
    @XmlElement(name = "BIC", required = true, nillable = true)
    protected String bic;

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the checkKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckKey() {
        return checkKey;
    }

    /**
     * Sets the value of the checkKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckKey(String value) {
        this.checkKey = value;
    }

    /**
     * Gets the value of the bban property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBBAN() {
        return bban;
    }

    /**
     * Sets the value of the bban property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBBAN(String value) {
        this.bban = value;
    }

    /**
     * Gets the value of the bic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBIC() {
        return bic;
    }

    /**
     * Sets the value of the bic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBIC(String value) {
        this.bic = value;
    }

}
