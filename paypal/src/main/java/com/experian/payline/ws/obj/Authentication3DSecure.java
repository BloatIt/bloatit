
package com.experian.payline.ws.obj;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains element for a 3DSecure
 * 						transaction
 * 					
 * 
 * <p>Java class for authentication3DSecure complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="authentication3DSecure">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="md" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pares" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="xid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="eci" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cavv" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cavvAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="vadsResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typeSecurisation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authentication3DSecure", propOrder = {
    "md",
    "pares",
    "xid",
    "eci",
    "cavv",
    "cavvAlgorithm",
    "vadsResult",
    "typeSecurisation"
})
public class Authentication3DSecure {

    @XmlElement(required = true, nillable = true)
    protected String md;
    @XmlElement(required = true, nillable = true)
    protected String pares;
    @XmlElement(required = true, nillable = true)
    protected String xid;
    @XmlElement(required = true, nillable = true)
    protected String eci;
    @XmlElement(required = true, nillable = true)
    protected String cavv;
    @XmlElement(required = true, nillable = true)
    protected String cavvAlgorithm;
    @XmlElement(required = true, nillable = true)
    protected String vadsResult;
    @XmlElement(required = true, nillable = true)
    protected String typeSecurisation;

    /**
     * Gets the value of the md property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMd() {
        return md;
    }

    /**
     * Sets the value of the md property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMd(String value) {
        this.md = value;
    }

    /**
     * Gets the value of the pares property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPares() {
        return pares;
    }

    /**
     * Sets the value of the pares property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPares(String value) {
        this.pares = value;
    }

    /**
     * Gets the value of the xid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXid() {
        return xid;
    }

    /**
     * Sets the value of the xid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXid(String value) {
        this.xid = value;
    }

    /**
     * Gets the value of the eci property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEci() {
        return eci;
    }

    /**
     * Sets the value of the eci property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEci(String value) {
        this.eci = value;
    }

    /**
     * Gets the value of the cavv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCavv() {
        return cavv;
    }

    /**
     * Sets the value of the cavv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCavv(String value) {
        this.cavv = value;
    }

    /**
     * Gets the value of the cavvAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCavvAlgorithm() {
        return cavvAlgorithm;
    }

    /**
     * Sets the value of the cavvAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCavvAlgorithm(String value) {
        this.cavvAlgorithm = value;
    }

    /**
     * Gets the value of the vadsResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVadsResult() {
        return vadsResult;
    }

    /**
     * Sets the value of the vadsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVadsResult(String value) {
        this.vadsResult = value;
    }

    /**
     * Gets the value of the typeSecurisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeSecurisation() {
        return typeSecurisation;
    }

    /**
     * Sets the value of the typeSecurisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeSecurisation(String value) {
        this.typeSecurisation = value;
    }

}
