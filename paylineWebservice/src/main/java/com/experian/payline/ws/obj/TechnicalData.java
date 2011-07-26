
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains technical data used to define acquirer service
 * 					
 * 
 * <p>Java class for technicalData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="technicalData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="GTInstance" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paymentProfil" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "technicalData", propOrder = {
    "terminalNumber",
    "gtInstance",
    "paymentProfil"
})
public class TechnicalData {

    @XmlElement(required = true, nillable = true)
    protected String terminalNumber;
    @XmlElement(name = "GTInstance", required = true, nillable = true)
    protected String gtInstance;
    @XmlElement(required = true, nillable = true)
    protected String paymentProfil;

    /**
     * Gets the value of the terminalNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminalNumber() {
        return terminalNumber;
    }

    /**
     * Sets the value of the terminalNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminalNumber(String value) {
        this.terminalNumber = value;
    }

    /**
     * Gets the value of the gtInstance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGTInstance() {
        return gtInstance;
    }

    /**
     * Sets the value of the gtInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGTInstance(String value) {
        this.gtInstance = value;
    }

    /**
     * Gets the value of the paymentProfil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentProfil() {
        return paymentProfil;
    }

    /**
     * Sets the value of the paymentProfil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentProfil(String value) {
        this.paymentProfil = value;
    }

}
