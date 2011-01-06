
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * functions availbe in virtual terminal
 * 
 * <p>Java class for virtualTerminalFunction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="virtualTerminalFunction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="function">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="simplePayment"/>
 *               &lt;enumeration value="walletCreation"/>
 *               &lt;enumeration value="nXPayment"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="functionParameter" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "virtualTerminalFunction", propOrder = {
    "function",
    "label",
    "functionParameter"
})
public class VirtualTerminalFunction {

    @XmlElement(required = true)
    protected String function;
    @XmlElement(required = true)
    protected String label;
    protected List<VirtualTerminalFunction.FunctionParameter> functionParameter;

    /**
     * Gets the value of the function property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunction(String value) {
        this.function = value;
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
     * Gets the value of the functionParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the functionParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFunctionParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualTerminalFunction.FunctionParameter }
     * 
     * 
     */
    public List<VirtualTerminalFunction.FunctionParameter> getFunctionParameter() {
        if (functionParameter == null) {
            functionParameter = new ArrayList<VirtualTerminalFunction.FunctionParameter>();
        }
        return this.functionParameter;
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
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FunctionParameter {

        @XmlAttribute(name = "id")
        @XmlSchemaType(name = "anySimpleType")
        protected String id;

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

    }

}
