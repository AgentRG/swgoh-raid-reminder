package utilities;

public class ReadyStrings {

    public final String wrongSyntax() {
        return "Wrong syntax. Please refer to --help";
    }

    public final String error() {
        return "Error!";
    }

    public final String success() {
        return "Success!";
    }

    public final String startingReminder() {
        return "Starting reminder...";
    }

    public final String noReminderSet() {
        return "No reminder was set in this channel.";
    }

    public final String minute() {
        return " minute.";
    }

    public final String minutes() {
        return " minutes.";
    }

    public final String thePitPrefix() {
        return "P";
    }

    public final String theTankTakedownPrefix() {
        return "T";
    }

    public final String theSithTriumviratePrefix() {
        return "S";
    }

    public final String thePitShortName() {
        return "pit";
    }

    public final String theTankTakedownShortName() {
        return "tank";
    }

    public final String theSithTriumvirateShortName() {
        return "sith";
    }

    public final String thePitFullName() {
        return "The Pit";
    }

    public final String theTankTakedownFullName() {
        return "The Tank Takedown";
    }

    public final String theSithTriumvirateFullName() {
        return "The Sith Triumvirate";
    }

    public String startReminderDescription(String raidName, long reminderTime, String minuteOrMinutes) {
        return "<@&547501790749261848> " + raidName + " is going to start in " + reminderTime + minuteOrMinutes;
    }
}