
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * virtualTerminal
 * 
 * <p>Java class for virtualTerminal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="virtualTerminal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="inactivityDelay" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="logo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="functions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="function" type="{http://obj.ws.payline.experian.com}virtualTerminalFunction" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "virtualTerminal", propOrder = {
    "label",
    "inactivityDelay",
    "logo",
    "functions"
})
public class VirtualTerminal {

    @XmlElement(required = true)
    protected String label;
    @XmlElement(defaultValue = "10")
    protected int inactivityDelay;
    protected String logo;
    @XmlElement(required = true)
    protected VirtualTerminal.Functions functions;

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
     * Gets the value of the inactivityDelay property.
     * 
     */
    public int getInactivityDelay() {
        return inactivityDelay;
    }

    /**
     * Sets the value of the inactivityDelay property.
     * 
     */
    public void setInactivityDelay(int value) {
        this.inactivityDelay = value;
    }

    /**
     * Gets the value of the logo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the value of the logo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogo(String value) {
        this.logo = value;
    }

    /**
     * Gets the value of the functions property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualTerminal.Functions }
     *     
     */
    public VirtualTerminal.Functions getFunctions() {
        return functions;
    }

    /**
     * Sets the value of the functions property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualTerminal.Functions }
     *     
     */
    public void setFunctions(VirtualTerminal.Functions value) {
        this.functions = value;
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
     *         &lt;element name="function" type="{http://obj.ws.payline.experian.com}virtualTerminalFunction" maxOccurs="unbounded"/>
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
        "function"
    })
    public static class Functions {

        @XmlElement(required = true)
        protected List<VirtualTerminalFunction> function;

        /**
         * Gets the value of the function property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the function property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFunction().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VirtualTerminalFunction }
         * 
         * 
         */
        public List<VirtualTerminalFunction> getFunction() {
            if (function == null) {
                function = new ArrayList<VirtualTerminalFunction>();
            }
            return this.function;
        }

    }

}
