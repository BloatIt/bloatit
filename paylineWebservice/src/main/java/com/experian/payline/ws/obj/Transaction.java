
package com.experian.payline.ws.obj;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains information about the
 * 						transaction
 * 					
 * 
 * <p>Java class for transaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isDuplicated" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isPossibleFraud" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fraudResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="explanation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="threeDSecure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="score" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transaction", propOrder = {
    "id",
    "date",
    "isDuplicated",
    "isPossibleFraud",
    "fraudResult",
    "explanation",
    "threeDSecure",
    "score"
})
public class Transaction {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String date;
    @XmlElement(required = true, nillable = true)
    protected String isDuplicated;
    @XmlElement(required = true)
    protected String isPossibleFraud;
    @XmlElement(required = true, nillable = true)
    protected String fraudResult;
    @XmlElement(required = true, nillable = true)
    protected String explanation;
    @XmlElementRef(name = "threeDSecure", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<String> threeDSecure;
    @XmlElement(required = true, nillable = true)
    protected String score;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

    /**
     * Gets the value of the isDuplicated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsDuplicated() {
        return isDuplicated;
    }

    /**
     * Sets the value of the isDuplicated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsDuplicated(String value) {
        this.isDuplicated = value;
    }

    /**
     * Gets the value of the isPossibleFraud property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsPossibleFraud() {
        return isPossibleFraud;
    }

    /**
     * Sets the value of the isPossibleFraud property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsPossibleFraud(String value) {
        this.isPossibleFraud = value;
    }

    /**
     * Gets the value of the fraudResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFraudResult() {
        return fraudResult;
    }

    /**
     * Sets the value of the fraudResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFraudResult(String value) {
        this.fraudResult = value;
    }

    /**
     * Gets the value of the explanation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets the value of the explanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExplanation(String value) {
        this.explanation = value;
    }

    /**
     * Gets the value of the threeDSecure property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getThreeDSecure() {
        return threeDSecure;
    }

    /**
     * Sets the value of the threeDSecure property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setThreeDSecure(JAXBElement<String> value) {
        this.threeDSecure = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScore(String value) {
        this.score = value;
    }

}
