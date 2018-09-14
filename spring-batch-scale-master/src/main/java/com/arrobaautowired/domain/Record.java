package com.arrobaautowired.domain;

import com.arrobaautowired.read.JaxbBigDecimalAdapter;
import com.arrobaautowired.read.JaxbDateAdapter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlRootElement(name = "record")
@Setter
@ToString(of = {"name", "income"})
public class Record {

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
