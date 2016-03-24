//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 01:57:18 PM CEST 
//


package eu.europa.esig.jaxb.tsl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceHistoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceHistoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ServiceHistoryInstance" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceHistoryType", propOrder = {
    "serviceHistoryInstance"
})
public class ServiceHistoryType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ServiceHistoryInstance")
    protected List<ServiceHistoryInstanceType> serviceHistoryInstance;

    /**
     * Gets the value of the serviceHistoryInstance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceHistoryInstance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceHistoryInstance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceHistoryInstanceType }
     * 
     * 
     */
    public List<ServiceHistoryInstanceType> getServiceHistoryInstance() {
        if (serviceHistoryInstance == null) {
            serviceHistoryInstance = new ArrayList<ServiceHistoryInstanceType>();
        }
        return this.serviceHistoryInstance;
    }

}
