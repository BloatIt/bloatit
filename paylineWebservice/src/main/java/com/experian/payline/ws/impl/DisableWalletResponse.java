
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.Result;
import com.experian.payline.ws.obj.WalletIdList;


/**
 * 
 * 							This element is the reponse from the
 * 							disableWallet method
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
 *         &lt;element name="walletIdList" type="{http://obj.ws.payline.experian.com}walletIdList"/>
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
    "walletIdList"
})
@XmlRootElement(name = "disableWalletResponse")
public class DisableWalletResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected WalletIdList walletIdList;

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
     * Gets the value of the walletIdList property.
     * 
     * @return
     *     possible object is
     *     {@link WalletIdList }
     *     
     */
    public WalletIdList getWalletIdList() {
        return walletIdList;
    }

    /**
     * Sets the value of the walletIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link WalletIdList }
     *     
     */
    public void setWalletIdList(WalletIdList value) {
        this.walletIdList = value;
    }

}
