
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.RefundAuthorizationList;


/**
 * 
 * 							This element is the request for the
 * 							doMassRefund method
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
 *         &lt;element name="refundAuthorizationList" type="{http://obj.ws.payline.experian.com}refundAuthorizationList"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "refundAuthorizationList",
    "comment"
})
@XmlRootElement(name = "doMassRefundRequest")
public class DoMassRefundRequest {

    @XmlElement(required = true, nillable = true)
    protected RefundAuthorizationList refundAuthorizationList;
    @XmlElement(required = true, nillable = true)
    protected String comment;

    /**
     * Gets the value of the refundAuthorizationList property.
     * 
     * @return
     *     possible object is
     *     {@link RefundAuthorizationList }
     *     
     */
    public RefundAuthorizationList getRefundAuthorizationList() {
        return refundAuthorizationList;
    }

    /**
     * Sets the value of the refundAuthorizationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefundAuthorizationList }
     *     
     */
    public void setRefundAuthorizationList(RefundAuthorizationList value) {
        this.refundAuthorizationList = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
