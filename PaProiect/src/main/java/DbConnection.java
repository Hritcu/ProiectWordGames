import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;

public class DbConnection {
    private static Connection con = null;

    private DbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java?autoReconnect=true&useSSL=false", "root", "JAVA");
        } catch (SQLException | ClassNotFoundException var2) {
            var2.printStackTrace();
        }

    }

    public static Connection getCon() {
        if (con == null) {
            new DbConnection();
        }

        return con;
    }
}
