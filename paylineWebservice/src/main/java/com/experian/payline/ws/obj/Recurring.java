
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains element for recurring
 * 						operation
 * 					
 * 
 * <p>Java class for recurring complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recurring">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="firstAmount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingCycle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingLeft" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingDay" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recurring", propOrder = {
    "firstAmount",
    "amount",
    "billingCycle",
    "billingLeft",
    "billingDay",
    "startDate"
})
public class Recurring {

    @XmlElement(required = true, nillable = true)
    protected String firstAmount;
    @XmlElement(required = true)
    protected String amount;
    @XmlElement(required = true)
    protected String billingCycle;
    @XmlElement(required = true)
    protected String billingLeft;
    @XmlElement(required = true, nillable = true)
    protected String billingDay;
    @XmlElement(required = true, nillable = true)
    protected String startDate;

    /**
     * Gets the value of the firstAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstAmount() {
        return firstAmount;
    }

    /**
     * Sets the value of the firstAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstAmount(String value) {
        this.firstAmount = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the billingCycle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCycle() {
        return billingCycle;
    }

    /**
     * Sets the value of the billingCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCycle(String value) {
        this.billingCycle = value;
    }

    /**
     * Gets the value of the billingLeft property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingLeft() {
        return billingLeft;
    }

    /**
     * Sets the value of the billingLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingLeft(String value) {
        this.billingLeft = value;
    }

    /**
     * Gets the value of the billingDay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingDay() {
        return billingDay;
    }

    /**
     * Sets the value of the billingDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingDay(String value) {
        this.billingDay = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

}
