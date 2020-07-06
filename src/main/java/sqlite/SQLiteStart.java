package sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class SQLiteStart {

    public String[] startReminder(String raid, String channel) {

        String sql = "SELECT * FROM reminders WHERE (raid='" + raid + "' AND room='" + channel + "')";
        String raidName = null;
        String channelResult = null;
        String timeResult = null;

        Connection connection = new SQLiteInitialize().openConnection();

        //Get list of all records that match the parameters.
        try (Statement initial_statement = connection.createStatement();
             ResultSet initial_result = initial_statement.executeQuery(sql)) {
            ArrayList<Long> createdTime = new ArrayList<>();
            System.out.println("Sent query: " + sql);
            while (initial_result.next()) {
                createdTime.add(Long.parseLong(initial_result.getString(5)));
            }
            try {
                //Get the newest record from the above query result.
            String createdTimeMax = Long.toString(createdTime.stream().mapToLong(v -> v).max().orElseThrow(NoSuchElementException::new));
            sql = "SELECT * FROM reminders WHERE (raid='" + raid + "' AND room='" + channel + "' AND created='" + createdTimeMax + "')";
            try(Statement secondary_statement = connection.createStatement();
                ResultSet secondary_result = secondary_statement.executeQuery(sql)) {
                if (secondary_result.next()) {
                    raidName = secondary_result.getString(2);
                    channelResult = secondary_result.getString(3);
                    timeResult = secondary_result.getString(4);
                }
                System.out.println("Sent query: " + sql);
            }} catch (Exception ignored) {}
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
        return new String[]{raidName, channelResult, timeResult};
    }
}
