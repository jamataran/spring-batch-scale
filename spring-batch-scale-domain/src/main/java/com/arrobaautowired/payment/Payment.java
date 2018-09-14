package com.arrobaautowired.payment;

import com.arrobaautowired.adapter.JaxbBigDecimalAdapter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlRootElement(name = "payment")
@Setter
@Builder
@EqualsAndHashCode
public class Payment implements Serializable {

    private String fullName;

    private String bic;

    private BigDecimal amount;

    private Character currency;

    @XmlAttribute(name = "fullname")
    public String getFullName() {
        return fullName;
    }

    @XmlAttribute(name="bic")
    public String getBic() {
        return bic;
    }
    @XmlJavaTypeAdapter(JaxbBigDecimalAdapter.class)
    @XmlElement(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @XmlAttribute(name = "currency")
    public Character getCurrency() {
        return currency;
    }
}
