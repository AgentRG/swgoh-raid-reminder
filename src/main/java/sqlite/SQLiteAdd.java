package sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

public class SQLiteAdd {

    public SQLiteAdd createNewRow(String raid, String room, String time) {

        String sql = "INSERT INTO reminders(raid, room, time, created) VALUES(?,?,?,?)";

        Connection connection = new SQLiteInitialize().openConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, raid);
            preparedStatement.setString(2, room);
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, Long.toString(Instant.now().getEpochSecond()));
            preparedStatement.executeUpdate();
            System.out.println("Sent query: INSERT INTO reminders(" + raid + "," + room + "," + time + "," + Instant.now().getEpochSecond() + ") VALUES(?,?,?,?)");
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
        return this;
    }
}
