
package com.experian.payline.ws.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.experian.payline.ws.obj.AddressInterlocutor;
import com.experian.payline.ws.obj.Interlocutor;
import com.experian.payline.ws.obj.PointOfSell;
import com.experian.payline.ws.obj.Subscription;


/**
 * 
 * 							This element is the request for the
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
 *         &lt;element name="corporateName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="publicName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nationalID">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="SIRET">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;length value="14"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="other" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="distributor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="merchantAddress" type="{http://obj.ws.payline.experian.com}addressInterlocutor"/>
 *         &lt;element name="businessInterlocutor" type="{http://obj.ws.payline.experian.com}interlocutor"/>
 *         &lt;element name="technicalInterlocutor" type="{http://obj.ws.payline.experian.com}interlocutor"/>
 *         &lt;element name="subscription" type="{http://obj.ws.payline.experian.com}subscription"/>
 *         &lt;element name="poss">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="pos" type="{http://obj.ws.payline.experian.com}pointOfSell" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="partner" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "corporateName",
    "publicName",
    "currency",
    "nationalID",
    "distributor",
    "merchantAddress",
    "businessInterlocutor",
    "technicalInterlocutor",
    "subscription",
    "poss",
    "partner"
})
@XmlRootElement(name = "createMerchantRequest")
public class CreateMerchantRequest {

    @XmlElement(required = true, nillable = true)
    protected String corporateName;
    @XmlElement(required = true, nillable = true)
    protected String publicName;
    @XmlElement(required = true)
    protected String currency;
    @XmlElement(required = true, nillable = true)
    protected CreateMerchantRequest.NationalID nationalID;
    @XmlElement(required = true, nillable = true)
    protected String distributor;
    @XmlElement(required = true, nillable = true)
    protected AddressInterlocutor merchantAddress;
    @XmlElement(required = true, nillable = true)
    protected Interlocutor businessInterlocutor;
    @XmlElement(required = true, nillable = true)
    protected Interlocutor technicalInterlocutor;
    @XmlElement(required = true, nillable = true)
    protected Subscription subscription;
    @XmlElement(required = true, nillable = true)
    protected CreateMerchantRequest.Poss poss;
    @XmlElement(required = true, nillable = true)
    protected String partner;

    /**
     * Gets the value of the corporateName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorporateName() {
        return corporateName;
    }

    /**
     * Sets the value of the corporateName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorporateName(String value) {
        this.corporateName = value;
    }

    /**
     * Gets the value of the publicName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicName() {
        return publicName;
    }

    /**
     * Sets the value of the publicName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicName(String value) {
        this.publicName = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the nationalID property.
     * 
     * @return
     *     possible object is
     *     {@link CreateMerchantRequest.NationalID }
     *     
     */
    public CreateMerchantRequest.NationalID getNationalID() {
        return nationalID;
    }

    /**
     * Sets the value of the nationalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateMerchantRequest.NationalID }
     *     
     */
    public void setNationalID(CreateMerchantRequest.NationalID value) {
        this.nationalID = value;
    }

    /**
     * Gets the value of the distributor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributor() {
        return distributor;
    }

    /**
     * Sets the value of the distributor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributor(String value) {
        this.distributor = value;
    }

    /**
     * Gets the value of the merchantAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInterlocutor }
     *     
     */
    public AddressInterlocutor getMerchantAddress() {
        return merchantAddress;
    }

    /**
     * Sets the value of the merchantAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInterlocutor }
     *     
     */
    public void setMerchantAddress(AddressInterlocutor value) {
        this.merchantAddress = value;
    }

    /**
     * Gets the value of the businessInterlocutor property.
     * 
     * @return
     *     possible object is
     *     {@link Interlocutor }
     *     
     */
    public Interlocutor getBusinessInterlocutor() {
        return businessInterlocutor;
    }

    /**
     * Sets the value of the businessInterlocutor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Interlocutor }
     *     
     */
    public void setBusinessInterlocutor(Interlocutor value) {
        this.businessInterlocutor = value;
    }

    /**
     * Gets the value of the technicalInterlocutor property.
     * 
     * @return
     *     possible object is
     *     {@link Interlocutor }
     *     
     */
    public Interlocutor getTechnicalInterlocutor() {
        return technicalInterlocutor;
    }

    /**
     * Sets the value of the technicalInterlocutor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Interlocutor }
     *     
     */
    public void setTechnicalInterlocutor(Interlocutor value) {
        this.technicalInterlocutor = value;
    }

    /**
     * Gets the value of the subscription property.
     * 
     * @return
     *     possible object is
     *     {@link Subscription }
     *     
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * Sets the value of the subscription property.
     * 
     * @param value
     *     allowed object is
     *     {@link Subscription }
     *     
     */
    public void setSubscription(Subscription value) {
        this.subscription = value;
    }

    /**
     * Gets the value of the poss property.
     * 
     * @return
     *     possible object is
     *     {@link CreateMerchantRequest.Poss }
     *     
     */
    public CreateMerchantRequest.Poss getPoss() {
        return poss;
    }

    /**
     * Sets the value of the poss property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateMerchantRequest.Poss }
     *     
     */
    public void setPoss(CreateMerchantRequest.Poss value) {
        this.poss = value;
    }

    /**
     * Gets the value of the partner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartner() {
        return partner;
    }

    /**
     * Sets the value of the partner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartner(String value) {
        this.partner = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="SIRET">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;length value="14"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="other" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "siret",
        "other"
    })
    public static class NationalID {

        @XmlElement(name = "SIRET")
        protected String siret;
        protected String other;

        /**
         * Gets the value of the siret property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSIRET() {
            return siret;
        }

        /**
         * Sets the value of the siret property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSIRET(String value) {
            this.siret = value;
        }

        /**
         * Gets the value of the other property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOther() {
            return other;
        }

        /**
         * Sets the value of the other property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOther(String value) {
            this.other = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="pos" type="{http://obj.ws.payline.experian.com}pointOfSell" maxOccurs="unbounded" minOccurs="0"/>
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
        "pos"
    })
    public static class Poss {

        @XmlElement(nillable = true)
        protected List<PointOfSell> pos;

        /**
         * Gets the value of the pos property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pos property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPos().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PointOfSell }
         * 
         * 
         */
        public List<PointOfSell> getPos() {
            if (pos == null) {
                pos = new ArrayList<PointOfSell>();
            }
            return this.pos;
        }

    }

}
