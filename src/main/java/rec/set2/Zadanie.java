package rec.set2;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Zadanie {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Zadanie.class.getName());
        if(args.length == 0)
        {
            logger.log(Level.SEVERE,"Please provide the input file name as the parameter");
            System.exit(0);
        }
        final Properties prop = loadProperties(logger);
        final String inputPath = args[0];
        try (Connection connection = DriverManager.getConnection(prop.getProperty("db.url"),
                prop.getProperty("db.user"), prop.getProperty("db.password"));
             PreparedStatement preparedStatement = connection
                     .prepareStatement("INSERT INTO customers(\"ID\",\"NAME\",\"SURNAME\",\"Age\") VALUES (?,?,?,?);");
             PreparedStatement preparedStatement2 = connection.prepareStatement(
                     "INSERT INTO contacts(\"ID\", \"ID_CUSTOMER\", \"TYPE\", \"CONTACT\") VALUES (?,?,?,?);")
        ){
            final String fileExtension = inputPath.substring(inputPath.length() - 4);
            if(fileExtension.equals(".xml")){
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                XMLHandler userHandler = new XMLHandler(preparedStatement, preparedStatement2);
                saxParser.parse(inputPath, userHandler);
            } else if (fileExtension.equals(".txt") || fileExtension.equals(".csv")){
                CSVHandler.handle(inputPath, preparedStatement, preparedStatement2);
            }
            else {
                logger.log(Level.SEVERE,"Unrecognized file type. Please provide a file with either 'csv' or 'xml' extension");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties(Logger logger) {
        Properties prop = new Properties();

        try (InputStream input = Zadanie.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.log(Level.SEVERE,"Sorry, unable to find config.properties");
            } else {
                prop.load(input);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE,"Property access failed: ");
        }
        return prop;
    }
}
