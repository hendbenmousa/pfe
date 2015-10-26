//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.2-hudson-jaxb-ri-2.2-63- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 01:57:18 PM CEST 
//


package eu.europa.esig.jaxb.ecc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KeyUsageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KeyUsageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="9">
 *         &lt;element name="KeyUsageBit" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}KeyUsageBitType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeyUsageType", propOrder = {
    "keyUsageBit"
})
public class KeyUsageType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "KeyUsageBit", required = true)
    protected List<KeyUsageBitType> keyUsageBit;

    /**
     * Gets the value of the keyUsageBit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyUsageBit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyUsageBit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyUsageBitType }
     * 
     * 
     */
    public List<KeyUsageBitType> getKeyUsageBit() {
        if (keyUsageBit == null) {
            keyUsageBit = new ArrayList<KeyUsageBitType>();
        }
        return this.keyUsageBit;
    }

}
