
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains the list of selected card
 * 					
 * 
 * <p>Java class for walletIdList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="walletIdList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="walletId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="500"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "walletIdList", propOrder = {
    "walletId"
})
public class WalletIdList {

    @XmlElement(required = true)
    protected List<String> walletId;

    /**
     * Gets the value of the walletId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the walletId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWalletId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getWalletId() {
        if (walletId == null) {
            walletId = new ArrayList<String>();
        }
        return this.walletId;
    }

}
