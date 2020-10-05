package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import utilities.ReadyStrings;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Start extends Command {

    private final String regex = "^rr.start (P|T|S|S2)$";
    private final ReadyStrings strings = new ReadyStrings();
    private String raidValue;

    public Start() {
        this.name = "start";
        this.help = "Start a raid reminder";
    }

    /**
     * The Start command collects the information provided by the user, parses SQLite to check if a record does exist, and and then starts a reminder.
     * @param commandEvent part of jdautilities and is used to build replies.
     */
    @Override
    protected void execute(CommandEvent commandEvent) {

        new Thread(() -> {
            if(commandEvent.getAuthor().isBot()) {
                return;
            }
            String content = commandEvent.getMessage().getContentRaw();
            String channel = String.valueOf(commandEvent.getChannel());
            if (content.matches(regex)) {
                buildGoodStartReply(commandEvent, channel, content);
            } else {
                buildBadStartReply(commandEvent );
            }
        }).start();
    }

    /**
     * @param content collects what was written by the user in the Add command.
     */
    private void setRaidName(String content) {
        String contentRaid = content.split(" ")[1];
        switch (contentRaid) {
            case "P":
                raidValue = strings.thePitShortName();
                break;
            case "T":
                raidValue = strings.theTankTakedownShortName();
                break;
            case "S":
                raidValue = strings.theSithTriumvirateShortName();
                break;
        }
    }

    /**
     * Calculates the time difference in minutes between the current reminder minute and wanted reminder minute.
     * @param currentTime accepts the current minute time in UTC.
     * @param reminderTime accepts the minute reminder from the record.
     * @return returns the time difference.
     * @throws ParseException
     */
    private long calculateTimeDifference(String currentTime, String reminderTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(currentTime);
        Date date2 = format.parse(reminderTime);
        long difference = date2.getTime() - date1.getTime();
        difference = TimeUnit.MILLISECONDS.toMinutes(difference);
        return difference;
    }

    /**
     * @return Returns the current time in UTC
     * @throws ParseException
     */
    private String getCurrentUtcTimeInString() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String time =  String.valueOf(localDateFormat.parse(simpleDateFormat.format(new Date())))
                .replaceAll(" ", "")
                .replaceAll("\\D", "")
                .substring(2);
        time = new StringBuilder(time).reverse().toString();
        time = time.substring(6);
        time = new StringBuilder(time).reverse().insert(2, ":").toString();
        time = time + ":00";
        return time;
    }

    /**
     * If the given syntax is correct, checks whether a record of the reminder does exist in the database.
     * If a record does not exist, throw an error.
     * If a record does exist, start a new timer thread on a 1 minute repeat until the difference between the UTC minute and reminder minute is 0. Proceeds to send a reminder message and ends thread.
     * @param commandEvent part of jdautilities and is used to build replies.
     * @param content collects what was written by the user in the Add command.
     * @param channel collects from which channel the message was sent in. Used by SQLite as a time (String) placeholder.
     */
    private void buildGoodStartReply(CommandEvent commandEvent, String channel, String content) {
        setRaidName(content);
        commandEvent.reply("oops");
    }

    /**
     * If the command given does not match the expected syntax, throw an error.
     * @param commandEvent part of jdautilities and is used to build replies.
     */
    private void buildBadStartReply(CommandEvent commandEvent) {
        commandEvent.reply(new EmbedBuilder()
                .setTitle(strings.error())
                .setColor(new Color(172, 25, 25))
                .setDescription(strings.wrongSyntax()).build());
    }
}
