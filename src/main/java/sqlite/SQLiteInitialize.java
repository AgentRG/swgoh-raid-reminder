package sqlite;

import utilities.Functions;

import java.sql.*;

public class SQLiteInitialize {

    Connection openConnection() {

        Connection connection = null;

        try {
            String url = "jdbc:sqlite:F:/Database/db/database.db";
            System.out.println("Opening connection");
            connection = DriverManager.getConnection(url);
            System.out.println("Connection established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public SQLiteInitialize newDatabase() {

        String databaseName = "database.db";
        String url = "jdbc:sqlite:F:/Database/db/" + databaseName;

        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("Database doesn't exist\n" +
                        "Creating new database\n" +
                        "Created new database\n" +
                        "Database name: " + databaseName + "\n" +
                        "Driver: " + meta.getDriverName());
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new Functions()
                .sleep(5);

        return this;
    }

    public void newTable() {

        Connection connection = openConnection();

        String sql = "CREATE TABLE IF NOT EXISTS reminders (id integer PRIMARY KEY, raid text NOT NULL, room text NOT NULL, time text NOT NULL, created text NOT NULL);";

        try {
            System.out.println("Creating new table.");
            Statement statement = connection.createStatement();
            new Functions().sleep(1);
            statement.execute(sql);
            System.out.println("Created new table");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        new Functions()
                .sleep(5);

    }
}