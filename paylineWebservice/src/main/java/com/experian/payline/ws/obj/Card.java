
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains information about the card
 * 					
 * 
 * <p>Java class for card complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="card">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="expirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cvx" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ownerBirthdayDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cardPresent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "card", propOrder = {
    "number",
    "type",
    "expirationDate",
    "cvx",
    "ownerBirthdayDate",
    "password",
    "cardPresent"
})
public class Card {

    @XmlElement(required = true)
    protected String number;
    @XmlElement(required = true)
    protected String type;
    @XmlElement(required = true, nillable = true)
    protected String expirationDate;
    @XmlElement(required = true, nillable = true)
    protected String cvx;
    @XmlElement(required = true, nillable = true)
    protected String ownerBirthdayDate;
    @XmlElement(required = true, nillable = true)
    protected String password;
    @XmlElement(required = true, nillable = true)
    protected String cardPresent;

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the cvx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCvx() {
        return cvx;
    }

    /**
     * Sets the value of the cvx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCvx(String value) {
        this.cvx = value;
    }

    /**
     * Gets the value of the ownerBirthdayDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerBirthdayDate() {
        return ownerBirthdayDate;
    }

    /**
     * Sets the value of the ownerBirthdayDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerBirthdayDate(String value) {
        this.ownerBirthdayDate = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the cardPresent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardPresent() {
        return cardPresent;
    }

    /**
     * Sets the value of the cardPresent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardPresent(String value) {
        this.cardPresent = value;
    }

}
