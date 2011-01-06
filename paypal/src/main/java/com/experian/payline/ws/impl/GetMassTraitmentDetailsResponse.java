
package com.experian.payline.ws.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.FailedListObject;
import com.experian.payline.ws.obj.Result;


/**
 * 
 * 							This element is the reponse from the
 * 							getMassTraitmentDetails method
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
 *         &lt;element name="massTraitementID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="totalLinesNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="failedLinesNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="failedListObject" type="{http://obj.ws.payline.experian.com}failedListObject"/>
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
    "massTraitementID",
    "totalLinesNumber",
    "failedLinesNumber",
    "failedListObject"
})
@XmlRootElement(name = "getMassTraitmentDetailsResponse")
public class GetMassTraitmentDetailsResponse {

    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected String massTraitementID;
    @XmlElement(required = true, nillable = true)
    protected String totalLinesNumber;
    @XmlElement(required = true, nillable = true)
    protected String failedLinesNumber;
    @XmlElement(required = true, nillable = true)
    protected FailedListObject failedListObject;

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
     * Gets the value of the massTraitementID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMassTraitementID() {
        return massTraitementID;
    }

    /**
     * Sets the value of the massTraitementID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMassTraitementID(String value) {
        this.massTraitementID = value;
    }

    /**
     * Gets the value of the totalLinesNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalLinesNumber() {
        return totalLinesNumber;
    }

    /**
     * Sets the value of the totalLinesNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalLinesNumber(String value) {
        this.totalLinesNumber = value;
    }

    /**
     * Gets the value of the failedLinesNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailedLinesNumber() {
        return failedLinesNumber;
    }

    /**
     * Sets the value of the failedLinesNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailedLinesNumber(String value) {
        this.failedLinesNumber = value;
    }

    /**
     * Gets the value of the failedListObject property.
     * 
     * @return
     *     possible object is
     *     {@link FailedListObject }
     *     
     */
    public FailedListObject getFailedListObject() {
        return failedListObject;
    }

    /**
     * Sets the value of the failedListObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailedListObject }
     *     
     */
    public void setFailedListObject(FailedListObject value) {
        this.failedListObject = value;
    }

}
