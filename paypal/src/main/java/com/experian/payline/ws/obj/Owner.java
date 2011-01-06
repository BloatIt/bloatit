
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains information about the
 * 						owner
 * 					
 * 
 * <p>Java class for owner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="owner">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingAddress" type="{http://obj.ws.payline.experian.com}addressOwner"/>
 *         &lt;element name="issueCardDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "owner", propOrder = {
    "lastName",
    "firstName",
    "billingAddress",
    "issueCardDate"
})
public class Owner {

    @XmlElement(required = true, nillable = true)
    protected String lastName;
    @XmlElement(required = true, nillable = true)
    protected String firstName;
    @XmlElement(required = true, nillable = true)
    protected AddressOwner billingAddress;
    @XmlElement(required = true, nillable = true)
    protected String issueCardDate;

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
     * Gets the value of the billingAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressOwner }
     *     
     */
    public AddressOwner getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the value of the billingAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressOwner }
     *     
     */
    public void setBillingAddress(AddressOwner value) {
        this.billingAddress = value;
    }

    /**
     * Gets the value of the issueCardDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueCardDate() {
        return issueCardDate;
    }

    /**
     * Sets the value of the issueCardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueCardDate(String value) {
        this.issueCardDate = value;
    }

}
