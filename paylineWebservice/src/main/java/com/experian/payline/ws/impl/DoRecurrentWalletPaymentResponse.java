
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.BillingRecordList;
import com.experian.payline.ws.obj.Result;


/**
 * 
 * 							This element is the reponse from the
 * 							doRecurrentWalletPayment method
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
 *         &lt;element name="paymentRecordId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="billingRecordList" type="{http://obj.ws.payline.experian.com}billingRecordList"/>
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
    "paymentRecordId",
    "billingRecordList"
})
@XmlRootElement(name = "doRecurrentWalletPaymentResponse")
public class DoRecurrentWalletPaymentResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected String paymentRecordId;
    @XmlElement(required = true)
    protected BillingRecordList billingRecordList;

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
     * Gets the value of the paymentRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentRecordId() {
        return paymentRecordId;
    }

    /**
     * Sets the value of the paymentRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentRecordId(String value) {
        this.paymentRecordId = value;
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

}
