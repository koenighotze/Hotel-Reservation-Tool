package org.koenighotze.jee7hotel.framework.application.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple adapter for handling dates in XML data.
 *
 * @author dschmitz
 */
public class DateAdapter extends XmlAdapter<String, Date> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
}
