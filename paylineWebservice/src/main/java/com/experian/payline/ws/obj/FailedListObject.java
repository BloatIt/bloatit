
package com.experian.payline.ws.obj;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 						An array of mass element failed
 * 					
 * 
 * <p>Java class for failedListObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="failedListObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="failedObject" type="{http://obj.ws.payline.experian.com}transaction" maxOccurs="5000"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "failedListObject", propOrder = {
    "failedObject"
})
public class FailedListObject {

    @XmlElement(required = true)
    protected List<Transaction> failedObject;

    /**
     * Gets the value of the failedObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the failedObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFailedObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaction }
     * 
     * 
     */
    public List<Transaction> getFailedObject() {
        if (failedObject == null) {
            failedObject = new ArrayList<Transaction>();
        }
        return this.failedObject;
    }

}
