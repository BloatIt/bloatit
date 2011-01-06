
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains bankAccount information
 * 					
 * 
 * <p>Java class for bankAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bankAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bankCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="iban" type="{http://obj.ws.payline.experian.com}iban"/>
 *         &lt;element name="rib" type="{http://obj.ws.payline.experian.com}rib"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bankAccount", propOrder = {
    "bankCode",
    "iban",
    "rib"
})
public class BankAccount {

    @XmlElement(required = true, nillable = true)
    protected String bankCode;
    @XmlElement(required = true, nillable = true)
    protected Iban iban;
    @XmlElement(required = true, nillable = true)
    protected Rib rib;

    /**
     * Gets the value of the bankCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * Sets the value of the bankCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankCode(String value) {
        this.bankCode = value;
    }

    /**
     * Gets the value of the iban property.
     * 
     * @return
     *     possible object is
     *     {@link Iban }
     *     
     */
    public Iban getIban() {
        return iban;
    }

    /**
     * Sets the value of the iban property.
     * 
     * @param value
     *     allowed object is
     *     {@link Iban }
     *     
     */
    public void setIban(Iban value) {
        this.iban = value;
    }

    /**
     * Gets the value of the rib property.
     * 
     * @return
     *     possible object is
     *     {@link Rib }
     *     
     */
    public Rib getRib() {
        return rib;
    }

    /**
     * Sets the value of the rib property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rib }
     *     
     */
    public void setRib(Rib value) {
        this.rib = value;
    }

}
