
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 							This element is the request for the
 * 							enableWallet method
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
 *         &lt;element name="contractNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "contractNumber",
    "walletId"
})
@XmlRootElement(name = "enableWalletRequest")
public class EnableWalletRequest {

    @XmlElement(required = true)
    protected String contractNumber;
    @XmlElement(required = true)
    protected String walletId;

    /**
     * Gets the value of the contractNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractNumber() {
        return contractNumber;
    }

    /**
     * Sets the value of the contractNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractNumber(String value) {
        this.contractNumber = value;
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
