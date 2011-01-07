
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains information about the
 * 						buyer
 * 					
 * 
 * <p>Java class for buyer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="buyer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shippingAdress" type="{http://obj.ws.payline.experian.com}address"/>
 *         &lt;element name="accountCreateDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accountAverageAmount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accountOrderCount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="walletId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobilePhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buyer", propOrder = {
    "lastName",
    "firstName",
    "email",
    "shippingAdress",
    "accountCreateDate",
    "accountAverageAmount",
    "accountOrderCount",
    "walletId",
    "ip",
    "mobilePhone"
})
public class Buyer {

    @XmlElement(required = true, nillable = true)
    protected String lastName;
    @XmlElement(required = true, nillable = true)
    protected String firstName;
    @XmlElement(required = true, nillable = true)
    protected String email;
    @XmlElement(required = true, nillable = true)
    protected Address shippingAdress;
    @XmlElement(required = true, nillable = true)
    protected String accountCreateDate;
    @XmlElement(required = true, nillable = true)
    protected String accountAverageAmount;
    @XmlElement(required = true, nillable = true)
    protected String accountOrderCount;
    @XmlElement(required = true, nillable = true)
    protected String walletId;
    @XmlElement(required = true, nillable = true)
    protected String ip;
    @XmlElement(required = true, nillable = true)
    protected String mobilePhone;

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the shippingAdress property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getShippingAdress() {
        return shippingAdress;
    }

    /**
     * Sets the value of the shippingAdress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setShippingAdress(Address value) {
        this.shippingAdress = value;
    }

    /**
     * Gets the value of the accountCreateDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCreateDate() {
        return accountCreateDate;
    }

    /**
     * Sets the value of the accountCreateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCreateDate(String value) {
        this.accountCreateDate = value;
    }

    /**
     * Gets the value of the accountAverageAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountAverageAmount() {
        return accountAverageAmount;
    }

    /**
     * Sets the value of the accountAverageAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountAverageAmount(String value) {
        this.accountAverageAmount = value;
    }

    /**
     * Gets the value of the accountOrderCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountOrderCount() {
        return accountOrderCount;
    }

    /**
     * Sets the value of the accountOrderCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountOrderCount(String value) {
        this.accountOrderCount = value;
    }

    /**
     * Gets the value of the walletId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWalletId() {
        return walletId;
    }

    /**
     * Sets the value of the walletId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWalletId(String value) {
        this.walletId = value;
    }

    /**
     * Gets the value of the ip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the value of the ip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIp(String value) {
        this.ip = value;
    }

    /**
     * Gets the value of the mobilePhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Sets the value of the mobilePhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhone(String value) {
        this.mobilePhone = value;
    }

}
