
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.ConnectionData;
import com.experian.payline.ws.obj.Result;


/**
 * 
 * 							This element is the reponse from the
 * 							createMerchant method
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
 *         &lt;element name="connectionData" type="{http://obj.ws.payline.experian.com}connectionData"/>
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
    "connectionData"
})
@XmlRootElement(name = "createMerchantResponse")
public class CreateMerchantResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected ConnectionData connectionData;

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
     * Gets the value of the connectionData property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionData }
     *     
     */
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    /**
     * Sets the value of the connectionData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionData }
     *     
     */
    public void setConnectionData(ConnectionData value) {
        this.connectionData = value;
    }

}
