package commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import sqlite.SQLiteAdd;
import utilities.ReadyStrings;

import java.awt.*;
import java.util.Arrays;

public class Add extends Command {

    private final ReadyStrings strings = new ReadyStrings();
    private final String correctPitRaidSyntax = "rr.add !" + strings.thePitPrefix() + "-.*\\d.*:.*\\d.*,.*\\d.*";
    private final String correctTankRaidSyntax = "rr.add !" + strings.theTankTakedownPrefix() + "-.*\\d.*:.*\\d.*,.*\\d.*";
    private final String correctSithRaidSyntax = "rr.add !" + strings.theSithTriumviratePrefix() + "-.*\\d.*:.*\\d.*,.*\\d.*";
    private String sqlRaidValue;
    private String raidName;

    public Add() {
        this.name = "add";
        this.help = "Add a new raid reminder";
    }

    /**
     * The Add commands collects information that was sent in the by the user and uses SQLite insertion to create a new row in the database.
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
            if (content.matches(correctPitRaidSyntax)
                    || content.matches(correctTankRaidSyntax)
                    || content.matches(correctSithRaidSyntax)) {
                buildGoodAddReply(commandEvent, content, channel);
            } else {
                buildBadAddReply(commandEvent);
            }
        }).start();
    }

    /**
     * @param content collects what was written by the user in the Add command.
     */
    private void setRaidPrefixAndRaidName(String content) {
        String contentRaid = content.split("!")[1].split("-")[0];
        switch (contentRaid) {
            case "P":
                sqlRaidValue = strings.thePitShortName();
                raidName = "_" + strings.thePitFullName() + "_";
                break;
            case "T":
                sqlRaidValue = strings.theTankTakedownShortName();
                raidName = "_" + strings.theTankTakedownFullName() + "_";
                break;
            case "S":
                sqlRaidValue = strings.theSithTriumvirateShortName();
                raidName = "_" + strings.theSithTriumvirateFullName() + "_";
                break;
        }
    }

    /**
     * @param content collects what was written by the user in the Add command.
     * @return returns the UTC time when the raid starts and how many minutes to send the reply prior to raid start.
     */
    private int[] extractAddData(String content) {
        String extractNumbers = content.replaceAll("[^0-9:,]", "");
        String removeColonAndComma = extractNumbers.replaceAll(":", " ");
        removeColonAndComma = removeColonAndComma.replaceAll(",", " ");
        return Arrays.stream(removeColonAndComma.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * If the given syntax is correct, inserts a new row in the database with the raid name, channel Id, raid start time and the reminder value.
     * If row was created successfully, send a reply in the channel.
     * @param commandEvent part of jdautilities and is used to build replies.
     * @param content collects what was written by the user in the Add command.
     * @param channel collects from which channel the message was sent in. Used by SQLite as a time (String) placeholder.
     */
    private void buildGoodAddReply(CommandEvent commandEvent, String content, String channel) {
        setRaidPrefixAndRaidName(content);
        String startTime;
        int[] individualNumbers = extractAddData(content);
        if (individualNumbers[0] > 23 || individualNumbers[1] > 59 || individualNumbers[2] > 60) {
            commandEvent.reply(new EmbedBuilder()
                    .setTitle(strings.error())
                    .setColor(new Color(172, 25, 25))
                    .addField("Hours:", individualNumbers[0] > 23 ? "Hours can't exceed 23 hours" : "✓", true)
                    .addField("Minutes:", individualNumbers[1] > 59 ? "Minutes can't exceed 59 minutes" : "✓", true)
                    .addField("Reminder:", individualNumbers[2] > 60 ? "Reminder can't exceed 60 minutes" : "✓", true)
                    .build());
        } else {
            //Bug fix. If the raid start time ends with :00, it would've posted a :0 instead in the confirmation reply.
            if (individualNumbers[1] == 0) {
                startTime = "00";
            } else {
                startTime = Integer.toString(individualNumbers[1]);
            }
            insetSqlLiteRow(channel, content);
            String minuteOrMinutes;
            if (individualNumbers[2] == 1) {
                minuteOrMinutes = strings.minute();
            } else {
                minuteOrMinutes =  strings.minutes();
            }
            commandEvent.reply(new EmbedBuilder()
                    .setTitle(strings.success())
                    .setColor(new Color(31, 161, 231))
                    .setDescription("Default " + raidName + " start time: " + individualNumbers[0] + ":" + startTime + "\n" +
                            "Reminder time set to: " + individualNumbers[2] + minuteOrMinutes).build());
        }
    }

    /**
     * If the command given does not match the expected syntax, throw an error.
     * @param commandEvent part of jdautilities and is used to build replies.
     */
    private void buildBadAddReply(CommandEvent commandEvent) {
        commandEvent.reply(new EmbedBuilder()
                .setTitle(strings.error())
                .setColor(new Color(172, 25, 25))
                .setDescription(strings.wrongSyntax()).build());
    }

    /**
     * Creates a new row in the reminders table.
     * @param sqlChannelValue collects from which channel the message was sent in.
     * @param content collects what was written by the user in the Add command.
     */
    private void insetSqlLiteRow(String sqlChannelValue, String content) {
        new SQLiteAdd()
                .createNewRow(sqlRaidValue, sqlChannelValue, sqlTimeValue(content));
    }

    /**
     * @param content collects what was written by the user in the Add command.
     * @return returns the UTC time when the raid starts and how many minutes to send the reply prior to raid start.
     */
    private String sqlTimeValue(String content) {
        return content.replaceAll("[^0-9:,]", "");
    }
}