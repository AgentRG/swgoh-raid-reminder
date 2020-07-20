package sqlite;

import utilities.Functions;

import java.sql.*;

public class SQLiteInitialize {

    /**
     * Path for the database will need to be set before the bot can function..
     */

    Connection openConnection() {

        Connection connection = null;

        try {
            String url = "jdbc:sqlite:"; //Set path to the database
            System.out.println("Opening connection");
            connection = DriverManager.getConnection(url);
            System.out.println("Connection established");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public SQLiteInitialize newDatabase() {

        String databaseName = "";
        String url = "jdbc:sqlite:"; //Set path + database name

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