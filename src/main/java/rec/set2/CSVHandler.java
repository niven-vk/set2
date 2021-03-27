package rec.set2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;

class CSVHandler {

    private CSVHandler(){}

    static void handle(String path, PreparedStatement customerStatement, PreparedStatement contactStatement) {
        int customerId = 1;
        int contactId = 1;
        try (
                BufferedReader br = new BufferedReader(new FileReader(path))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                customerStatement.setInt(1,customerId);
                customerStatement.setString(2, values[0]);
                customerStatement.setString(3, values[1]);
                customerStatement.setString(4, values[2].equals("") ? null : values[2]);
                contactStatement.setInt(2,customerId++);
                for (int i = 4; i < values.length; i++) {
                    contactStatement.setInt(1,contactId++);
                    contactStatement.setInt(3, getTypeOf(values[i]));
                    contactStatement.setString(4,values[i]);
                    contactStatement.addBatch();
                    if(contactId % 500 == 0) contactStatement.executeBatch();
                }
                customerStatement.addBatch();
                if(customerId % 500 == 0) customerStatement.executeBatch();

            }
            customerStatement.executeBatch();
            contactStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    private static int getTypeOf(String value) {
        if (value.matches("^(.+)@(.+)\\.(.+)$")) {
            return 1;
        } else if (value.matches("^(\\d{3}[- .]?){2}\\d{3}$")){
            return 2;
        } else if (value.matches("^(.+)@(.+)$")) {
            return  3;
        } else return 0;
    }
}
