package com.arrobaautowired.record;

import com.arrobaautowired.adapter.JaxbBigDecimalAdapter;
import com.arrobaautowired.adapter.JaxbDateAdapter;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "record")
@Setter
@ToString(of = {"name", "income"})
@EqualsAndHashCode
public class Record implements Serializable {

    private int refId;
    private String name;
    private int age;
    private Date dob;
    private BigDecimal income;

    @XmlAttribute(name = "refId")
    public int getRefId() {
        return refId;
    }

    @XmlElement(name = "age")
    public int getAge() {
        return age;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    @XmlElement
    public Date getDob() {
        return dob;
    }


    @XmlJavaTypeAdapter(JaxbBigDecimalAdapter.class)
    @XmlElement
    public BigDecimal getIncome() {
        return income;
    }

}
