import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:weather.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS weather_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "city TEXT NOT NULL," +
                    "temperature REAL," +
                    "description TEXT," +
                    "datetime TEXT" +
                    ")";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database insert error: " + e.getMessage());
        }
    }

    public static void insertWeather(String city, double temp, String desc, String datetime) {
        String sql = "INSERT INTO weather_history (city, temperature, description, datetime) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, city);
            pstmt.setDouble(2, temp);
            pstmt.setString(3, desc);
            pstmt.setString(4, datetime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Database insert error: " + e.getMessage());
        }
    }
}
