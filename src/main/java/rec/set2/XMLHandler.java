package rec.set2;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XMLHandler extends DefaultHandler {

    private boolean contacts = false;
    private int customerId = 1;
    private int contactId = 1;
    private String elementValue;
    private PreparedStatement customersStatement;
    private PreparedStatement contactStatement;

    XMLHandler(PreparedStatement preparedStatement, PreparedStatement preparedStatement2) {
        customersStatement = preparedStatement;
        contactStatement = preparedStatement2;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("person")) {
            try {
                customersStatement.setInt(1,customerId);
                customersStatement.setString(4, null);
                contactStatement.setInt(2, customerId++);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (qName.equalsIgnoreCase("contacts")) {
            contacts = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        try {
            switch (qName) {
                case "name":
                    customersStatement.setString(2, elementValue);
                    break;
                case "surname":
                    customersStatement.setString(3, elementValue);
                    break;
                case "age":
                    customersStatement.setInt(4, Integer.parseInt(elementValue));
                    break;
                case "email":
                    contactStatement.setInt(3, 1);
                    break;
                case "phone":
                    contactStatement.setInt(3, 2);
                    break;
                case "jabber":
                    contactStatement.setInt(3, 3);
                    break;
                case "contacts":
                    contacts = false;
                    break;
                case "person":
                    customersStatement.addBatch();
                    if (customerId % 500 == 0) customersStatement.executeBatch();
                    break;
                case "persons":
                    customersStatement.executeBatch();
                    contactStatement.executeBatch();
                    break;
                default:
                    if (contacts) contactStatement.setInt(3, 0);
            }
                if (contacts) {
                    contactStatement.setInt(1, contactId++);
                    contactStatement.setString(4, elementValue);
                    contactStatement.addBatch();
                    if (contactId % 500 == 0) contactStatement.executeBatch();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        elementValue = new String(ch, start, length);
    }
}