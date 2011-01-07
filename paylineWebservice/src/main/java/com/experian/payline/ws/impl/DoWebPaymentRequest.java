
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.Buyer;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.Owner;
import com.experian.payline.ws.obj.Payment;
import com.experian.payline.ws.obj.PrivateDataList;
import com.experian.payline.ws.obj.Recurring;
import com.experian.payline.ws.obj.SelectedContractList;


/**
 * 
 * 							This element is the request for the
 * 							doWebPayment method
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
 *         &lt;element name="payment" type="{http://obj.ws.payline.experian.com}payment"/>
 *         &lt;element name="returnURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cancelURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="order" type="{http://obj.ws.payline.experian.com}order"/>
 *         &lt;element name="notificationURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="selectedContractList" type="{http://obj.ws.payline.experian.com}selectedContractList"/>
 *         &lt;element name="privateDataList" type="{http://obj.ws.payline.experian.com}privateDataList"/>
 *         &lt;element name="languageCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customPaymentPageCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="buyer" type="{http://obj.ws.payline.experian.com}buyer"/>
 *         &lt;element name="owner" type="{http://obj.ws.payline.experian.com}owner"/>
 *         &lt;element name="securityMode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recurring" type="{http://obj.ws.payline.experian.com}recurring"/>
 *         &lt;element name="customPaymentTemplateURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "payment",
    "returnURL",
    "cancelURL",
    "order",
    "notificationURL",
    "selectedContractList",
    "privateDataList",
    "languageCode",
    "customPaymentPageCode",
    "buyer",
    "owner",
    "securityMode",
    "recurring",
    "customPaymentTemplateURL"
})
@XmlRootElement(name = "doWebPaymentRequest")
public class DoWebPaymentRequest {

    @XmlElement(required = true)
    protected Payment payment;
    @XmlElement(required = true)
    protected String returnURL;
    @XmlElement(required = true)
    protected String cancelURL;
    @XmlElement(required = true)
    protected Order order;
    @XmlElement(required = true, nillable = true)
    protected String notificationURL;
    @XmlElement(required = true, nillable = true)
    protected SelectedContractList selectedContractList;
    @XmlElement(required = true, nillable = true)
    protected PrivateDataList privateDataList;
    @XmlElement(required = true, nillable = true)
    protected String languageCode;
    @XmlElement(required = true, nillable = true)
    protected String customPaymentPageCode;
    @XmlElement(required = true, nillable = true)
    protected Buyer buyer;
    @XmlElement(required = true, nillable = true)
    protected Owner owner;
    @XmlElement(required = true, nillable = true)
    protected String securityMode;
    @XmlElement(required = true, nillable = true)
    protected Recurring recurring;
    @XmlElement(required = true, nillable = true)
    protected String customPaymentTemplateURL;

    /**
     * Gets the value of the payment property.
     * 
     * @return
     *     possible object is
     *     {@link Payment }
     *     
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * Sets the value of the payment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Payment }
     *     
     */
    public void setPayment(Payment value) {
        this.payment = value;
    }

    /**
     * Gets the value of the returnURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnURL() {
        return returnURL;
    }

    /**
     * Sets the value of the returnURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnURL(String value) {
        this.returnURL = value;
    }

    /**
     * Gets the value of the cancelURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelURL() {
        return cancelURL;
    }

    /**
     * Sets the value of the cancelURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelURL(String value) {
        this.cancelURL = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link Order }
     *     
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link Order }
     *     
     */
    public void setOrder(Order value) {
        this.order = value;
    }

    /**
     * Gets the value of the notificationURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationURL() {
        return notificationURL;
    }

    /**
     * Sets the value of the notificationURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationURL(String value) {
        this.notificationURL = value;
    }

    /**
     * Gets the value of the selectedContractList property.
     * 
     * @return
     *     possible object is
     *     {@link SelectedContractList }
     *     
     */
    public SelectedContractList getSelectedContractList() {
        return selectedContractList;
    }

    /**
     * Sets the value of the selectedContractList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SelectedContractList }
     *     
     */
    public void setSelectedContractList(SelectedContractList value) {
        this.selectedContractList = value;
    }

    /**
     * Gets the value of the privateDataList property.
     * 
     * @return
     *     possible object is
     *     {@link PrivateDataList }
     *     
     */
    public PrivateDataList getPrivateDataList() {
        return privateDataList;
    }

    /**
     * Sets the value of the privateDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrivateDataList }
     *     
     */
    public void setPrivateDataList(PrivateDataList value) {
        this.privateDataList = value;
    }

    /**
     * Gets the value of the languageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguageCode(String value) {
        this.languageCode = value;
    }

    /**
     * Gets the value of the customPaymentPageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomPaymentPageCode() {
        return customPaymentPageCode;
    }

    /**
     * Sets the value of the customPaymentPageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomPaymentPageCode(String value) {
        this.customPaymentPageCode = value;
    }

    /**
     * Gets the value of the buyer property.
     * 
     * @return
     *     possible object is
     *     {@link Buyer }
     *     
     */
    public Buyer getBuyer() {
        return buyer;
    }

    /**
     * Sets the value of the buyer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Buyer }
     *     
     */
    public void setBuyer(Buyer value) {
        this.buyer = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link Owner }
     *     
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Owner }
     *     
     */
    public void setOwner(Owner value) {
        this.owner = value;
    }

    /**
     * Gets the value of the securityMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityMode() {
        return securityMode;
    }

    /**
     * Sets the value of the securityMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityMode(String value) {
        this.securityMode = value;
    }

    /**
     * Gets the value of the recurring property.
     * 
     * @return
     *     possible object is
     *     {@link Recurring }
     *     
     */
    public Recurring getRecurring() {
        return recurring;
    }

    /**
     * Sets the value of the recurring property.
     * 
     * @param value
     *     allowed object is
     *     {@link Recurring }
     *     
     */
    public void setRecurring(Recurring value) {
        this.recurring = value;
    }

    /**
     * Gets the value of the customPaymentTemplateURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomPaymentTemplateURL() {
        return customPaymentTemplateURL;
    }

    /**
     * Sets the value of the customPaymentTemplateURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomPaymentTemplateURL(String value) {
        this.customPaymentTemplateURL = value;
    }

}
