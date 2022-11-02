import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicApp {

    String jdbcString = "dm.jdbc.driver.DmDriver";

    String urlString = "jdbc:dm://172.20.101.44:5236";

    String userName = "envision";

    String password = "dm_envision";

    Connection conn = null;

    public void loadJdbcDriver() throws SQLException {
        try {
            System.out.println("Loading JDBC Driver...");
            Class.forName(jdbcString);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Load JDBC Driver Error1: " + e.getMessage());
        } catch (Exception ex) {
            throw new SQLException("Load JDBC Driver Error : "
                    + ex.getMessage());
        }
    }

    public void connect() throws SQLException {
        try {
            System.out.println("Connecting to DM Server...");
            conn = DriverManager.getConnection(urlString, userName, password);
        } catch (SQLException e) {
            throw new SQLException("Connect to DM Server Error : "
                    + e.getMessage());
        }
    }

    public void disConnect() throws SQLException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new SQLException("close connection error : " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            BasicApp basicApp = new BasicApp();
            basicApp.loadJdbcDriver();
            basicApp.connect();
            basicApp.disConnect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
