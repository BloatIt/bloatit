
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.Authentication3DSecure;
import com.experian.payline.ws.obj.Result;


/**
 * 
 * 							This element is the reponse from the
 * 							verifyEnrollment method
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
 *         &lt;element name="actionUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actionMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pareqFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pareqFieldValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="termUrlName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="termUrlValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mdFieldName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mdFieldValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mpiResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authentication3DSecure" type="{http://obj.ws.payline.experian.com}authentication3DSecure"/>
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
    "actionUrl",
    "actionMethod",
    "pareqFieldName",
    "pareqFieldValue",
    "termUrlName",
    "termUrlValue",
    "mdFieldName",
    "mdFieldValue",
    "mpiResult",
    "authentication3DSecure"
})
@XmlRootElement(name = "verifyEnrollmentResponse")
public class VerifyEnrollmentResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true, nillable = true)
    protected String actionUrl;
    @XmlElement(required = true, nillable = true)
    protected String actionMethod;
    @XmlElement(required = true, nillable = true)
    protected String pareqFieldName;
    @XmlElement(required = true, nillable = true)
    protected String pareqFieldValue;
    @XmlElement(required = true, nillable = true)
    protected String termUrlName;
    @XmlElement(required = true, nillable = true)
    protected String termUrlValue;
    @XmlElement(required = true, nillable = true)
    protected String mdFieldName;
    @XmlElement(required = true, nillable = true)
    protected String mdFieldValue;
    @XmlElement(required = true, nillable = true)
    protected String mpiResult;
    @XmlElement(required = true, nillable = true)
    protected Authentication3DSecure authentication3DSecure;

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
     * Gets the value of the actionUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionUrl() {
        return actionUrl;
    }

    /**
     * Sets the value of the actionUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionUrl(String value) {
        this.actionUrl = value;
    }

    /**
     * Gets the value of the actionMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionMethod() {
        return actionMethod;
    }

    /**
     * Sets the value of the actionMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionMethod(String value) {
        this.actionMethod = value;
    }

    /**
     * Gets the value of the pareqFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPareqFieldName() {
        return pareqFieldName;
    }

    /**
     * Sets the value of the pareqFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPareqFieldName(String value) {
        this.pareqFieldName = value;
    }

    /**
     * Gets the value of the pareqFieldValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPareqFieldValue() {
        return pareqFieldValue;
    }

    /**
     * Sets the value of the pareqFieldValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPareqFieldValue(String value) {
        this.pareqFieldValue = value;
    }

    /**
     * Gets the value of the termUrlName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermUrlName() {
        return termUrlName;
    }

    /**
     * Sets the value of the termUrlName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermUrlName(String value) {
        this.termUrlName = value;
    }

    /**
     * Gets the value of the termUrlValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermUrlValue() {
        return termUrlValue;
    }

    /**
     * Sets the value of the termUrlValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermUrlValue(String value) {
        this.termUrlValue = value;
    }

    /**
     * Gets the value of the mdFieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdFieldName() {
        return mdFieldName;
    }

    /**
     * Sets the value of the mdFieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdFieldName(String value) {
        this.mdFieldName = value;
    }

    /**
     * Gets the value of the mdFieldValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdFieldValue() {
        return mdFieldValue;
    }

    /**
     * Sets the value of the mdFieldValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdFieldValue(String value) {
        this.mdFieldValue = value;
    }

    /**
     * Gets the value of the mpiResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMpiResult() {
        return mpiResult;
    }

    /**
     * Sets the value of the mpiResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMpiResult(String value) {
        this.mpiResult = value;
    }

    /**
     * Gets the value of the authentication3DSecure property.
     * 
     * @return
     *     possible object is
     *     {@link Authentication3DSecure }
     *     
     */
    public Authentication3DSecure getAuthentication3DSecure() {
        return authentication3DSecure;
    }

    /**
     * Sets the value of the authentication3DSecure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authentication3DSecure }
     *     
     */
    public void setAuthentication3DSecure(Authentication3DSecure value) {
        this.authentication3DSecure = value;
    }

}
