import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class dbConnection {

//    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
//    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/motech_data_services";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "password";

    public static Map<String, List<Object>> getAllIndependentChildRecords(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) {

        try {

            Map<String, List<Object>> childList = selectAllIndependentChildRecords(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
            return childList;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
        return null;

    }

    private static Map<String, List<Object>> selectAllIndependentChildRecords(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;

        String queryString = "SELECT nms_mcts_children.beneficiaryId, nms_subscribers.id, nms_subscribers.dateOfBirth "
                +"FROM nms_mcts_children LEFT JOIN nms_subscribers "
                +"ON nms_mcts_children.id = nms_subscribers.child_id_OID "
                +"WHERE nms_mcts_children.mother_id_OID IS NULL";

        try {
            dbConnection = getDBConnection(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
            statement = dbConnection.createStatement();

            System.out.println(queryString);

            // execute select SQL stetement
            ResultSet rs = statement.executeQuery(queryString);
            Map<String, List<Object>> map = new HashMap<String, List<Object>>();

            while (rs.next()) {

                String beneficiaryId = rs.getString("beneficiaryId");
                Long id = rs.getLong("id");
                id = (id == 0) ? null : id;
                Date dob = rs.getDate("dateOfBirth");
                List<Object> list = new ArrayList<Object>(Arrays.asList(id,dob));
                map.put(beneficiaryId, list);
            }
            return map;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
        return  null;

    }

    private static Connection getDBConnection(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) {

        Connection dbConnection = null;

        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

}