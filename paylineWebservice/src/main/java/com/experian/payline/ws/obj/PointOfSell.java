
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						This element contains all information about point of sell
 * 					
 * 
 * <p>Java class for pointOfSell complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pointOfSell">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="siret" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codeMcc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="webmasterEmail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webstoreURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="notificationURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="privateLifeURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saleCondURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buyerMustAcceptSaleCond" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="endOfPaymentRedirection" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ticketSend" type="{http://obj.ws.payline.experian.com}ticketSend"/>
 *         &lt;element name="contracts">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="contract" type="{http://obj.ws.payline.experian.com}contract" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="virtualTerminal" type="{http://obj.ws.payline.experian.com}virtualTerminal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pointOfSell", propOrder = {
    "siret",
    "codeMcc",
    "label",
    "webmasterEmail",
    "comments",
    "webstoreURL",
    "notificationURL",
    "privateLifeURL",
    "saleCondURL",
    "buyerMustAcceptSaleCond",
    "endOfPaymentRedirection",
    "ticketSend",
    "contracts",
    "virtualTerminal"
})
public class PointOfSell {

    @XmlElement(required = true, nillable = true)
    protected String siret;
    @XmlElement(required = true, nillable = true)
    protected String codeMcc;
    @XmlElement(required = true, nillable = true)
    protected String label;
    @XmlElement(required = true, nillable = true)
    protected String webmasterEmail;
    @XmlElementRef(name = "comments", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<String> comments;
    @XmlElement(required = true, nillable = true)
    protected String webstoreURL;
    @XmlElement(required = true, nillable = true)
    protected String notificationURL;
    @XmlElementRef(name = "privateLifeURL", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<String> privateLifeURL;
    @XmlElementRef(name = "saleCondURL", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<String> saleCondURL;
    @XmlElementRef(name = "buyerMustAcceptSaleCond", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> buyerMustAcceptSaleCond;
    @XmlElementRef(name = "endOfPaymentRedirection", namespace = "http://obj.ws.payline.experian.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> endOfPaymentRedirection;
    @XmlElement(required = true, nillable = true)
    protected TicketSend ticketSend;
    @XmlElement(required = true)
    protected PointOfSell.Contracts contracts;
    @XmlElement(required = true, nillable = true)
    protected VirtualTerminal virtualTerminal;

    /**
     * Gets the value of the siret property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiret() {
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
    public void setSiret(String value) {
        this.siret = value;
    }

    /**
     * Gets the value of the codeMcc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeMcc() {
        return codeMcc;
    }

    /**
     * Sets the value of the codeMcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeMcc(String value) {
        this.codeMcc = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the webmasterEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebmasterEmail() {
        return webmasterEmail;
    }

    /**
     * Sets the value of the webmasterEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebmasterEmail(String value) {
        this.webmasterEmail = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setComments(JAXBElement<String> value) {
        this.comments = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the webstoreURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebstoreURL() {
        return webstoreURL;
    }

    /**
     * Sets the value of the webstoreURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebstoreURL(String value) {
        this.webstoreURL = value;
    }

    /**
     * Gets the value of the notificationURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationURL() {
        return notificationURL;
    }

    /**
     * Sets the value of the notificationURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationURL(String value) {
        this.notificationURL = value;
    }

    /**
     * Gets the value of the privateLifeURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPrivateLifeURL() {
        return privateLifeURL;
    }

    /**
     * Sets the value of the privateLifeURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPrivateLifeURL(JAXBElement<String> value) {
        this.privateLifeURL = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the saleCondURL property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSaleCondURL() {
        return saleCondURL;
    }

    /**
     * Sets the value of the saleCondURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSaleCondURL(JAXBElement<String> value) {
        this.saleCondURL = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the buyerMustAcceptSaleCond property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getBuyerMustAcceptSaleCond() {
        return buyerMustAcceptSaleCond;
    }

    /**
     * Sets the value of the buyerMustAcceptSaleCond property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setBuyerMustAcceptSaleCond(JAXBElement<Boolean> value) {
        this.buyerMustAcceptSaleCond = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the endOfPaymentRedirection property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getEndOfPaymentRedirection() {
        return endOfPaymentRedirection;
    }

    /**
     * Sets the value of the endOfPaymentRedirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setEndOfPaymentRedirection(JAXBElement<Boolean> value) {
        this.endOfPaymentRedirection = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the ticketSend property.
     * 
     * @return
     *     possible object is
     *     {@link TicketSend }
     *     
     */
    public TicketSend getTicketSend() {
        return ticketSend;
    }

    /**
     * Sets the value of the ticketSend property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketSend }
     *     
     */
    public void setTicketSend(TicketSend value) {
        this.ticketSend = value;
    }

    /**
     * Gets the value of the contracts property.
     * 
     * @return
     *     possible object is
     *     {@link PointOfSell.Contracts }
     *     
     */
    public PointOfSell.Contracts getContracts() {
        return contracts;
    }

    /**
     * Sets the value of the contracts property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointOfSell.Contracts }
     *     
     */
    public void setContracts(PointOfSell.Contracts value) {
        this.contracts = value;
    }

    /**
     * Gets the value of the virtualTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualTerminal }
     *     
     */
    public VirtualTerminal getVirtualTerminal() {
        return virtualTerminal;
    }

    /**
     * Sets the value of the virtualTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualTerminal }
     *     
     */
    public void setVirtualTerminal(VirtualTerminal value) {
        this.virtualTerminal = value;
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
     *         &lt;element name="contract" type="{http://obj.ws.payline.experian.com}contract" maxOccurs="unbounded" minOccurs="0"/>
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
        "contract"
    })
    public static class Contracts {

        protected List<Contract> contract;

        /**
         * Gets the value of the contract property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the contract property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContract().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Contract }
         * 
         * 
         */
        public List<Contract> getContract() {
            if (contract == null) {
                contract = new ArrayList<Contract>();
            }
            return this.contract;
        }

    }

}
