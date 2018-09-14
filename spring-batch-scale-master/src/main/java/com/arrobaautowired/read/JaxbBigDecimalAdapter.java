package com.arrobaautowired.read;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class JaxbBigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public String marshal(BigDecimal obj) {
        return obj.toString();
    }

    @Override
    public BigDecimal unmarshal(String obj) {
        return new BigDecimal(obj.replaceAll(",", ""));
    }

}