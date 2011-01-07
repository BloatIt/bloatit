
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.BillingRecordList;
import com.experian.payline.ws.obj.Order;
import com.experian.payline.ws.obj.PrivateDataList;
import com.experian.payline.ws.obj.Recurring;
import com.experian.payline.ws.obj.Result;


/**
 * 
 * 							This element is the reponse from the
 * 							getPaymentRecord method
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
 *         &lt;element name="result" type="{http://obj.ws.payline.experian.com}result"/>
 *         &lt;element name="recurring" type="{http://obj.ws.payline.experian.com}recurring"/>
 *         &lt;element name="isDisabled" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="disableDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingRecordList" type="{http://obj.ws.payline.experian.com}billingRecordList"/>
 *         &lt;element name="privateDataList" type="{http://obj.ws.payline.experian.com}privateDataList"/>
 *         &lt;element name="order" type="{http://obj.ws.payline.experian.com}order"/>
 *         &lt;element name="walletId" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "result",
    "recurring",
    "isDisabled",
    "disableDate",
    "billingRecordList",
    "privateDataList",
    "order",
    "walletId"
})
@XmlRootElement(name = "getPaymentRecordResponse")
public class GetPaymentRecordResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected Recurring recurring;
    @XmlElement(required = true, nillable = true)
    protected String isDisabled;
    @XmlElement(required = true, nillable = true)
    protected String disableDate;
    @XmlElement(required = true)
    protected BillingRecordList billingRecordList;
    @XmlElement(required = true, nillable = true)
    protected PrivateDataList privateDataList;
    @XmlElement(required = true, nillable = true)
    protected Order order;
    @XmlElement(required = true)
    protected String walletId;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Result }
     *     
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Result }
     *     
     */
    public void setResult(Result value) {
        this.result = value;
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
     * Gets the value of the isDisabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsDisabled() {
        return isDisabled;
    }

    /**
     * Sets the value of the isDisabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsDisabled(String value) {
        this.isDisabled = value;
    }

    /**
     * Gets the value of the disableDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisableDate() {
        return disableDate;
    }

    /**
     * Sets the value of the disableDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisableDate(String value) {
        this.disableDate = value;
    }

    /**
     * Gets the value of the billingRecordList property.
     * 
     * @return
     *     possible object is
     *     {@link BillingRecordList }
     *     
     */
    public BillingRecordList getBillingRecordList() {
        return billingRecordList;
    }

    /**
     * Sets the value of the billingRecordList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingRecordList }
     *     
     */
    public void setBillingRecordList(BillingRecordList value) {
        this.billingRecordList = value;
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

}
