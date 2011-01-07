
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						An array of authorization to refund
 * 					
 * 
 * <p>Java class for refundAuthorizationList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refundAuthorizationList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="refund" type="{http://obj.ws.payline.experian.com}refund" maxOccurs="5000"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refundAuthorizationList", propOrder = {
    "refund"
})
public class RefundAuthorizationList {

    @XmlElement(required = true)
    protected List<Refund> refund;

    /**
     * Gets the value of the refund property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the refund property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRefund().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Refund }
     * 
     * 
     */
    public List<Refund> getRefund() {
        if (refund == null) {
            refund = new ArrayList<Refund>();
        }
        return this.refund;
    }

}
