import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.Add;
import commands.Start;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.log4j.BasicConfigurator;
import sqlite.SQLiteInitialize;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RaidReminder {

    public static void main (String[] args) throws IOException, LoginException, InterruptedException {

        File database = new File("F:/Database/db/database.db");

        if (!database.exists()) {
            new SQLiteInitialize()
                    .newDatabase()
                    .newTable();
        }

        List<String> configFile = Files.readAllLines(Paths.get("F:/Database/config.txt"));
        BasicConfigurator.configure();
        EventWaiter waiter = new EventWaiter();

        String token = configFile.get(0);
        String ownerId = configFile.get(1);

        CommandClientBuilder client = new CommandClientBuilder()
                .setActivity(Activity.listening("rr.help"))
                .setStatus(OnlineStatus.ONLINE)
                .setOwnerId(ownerId)
                .setPrefix("rr.")
                .addCommands(
                        new Add(),
                        new Start());

        JDABuilder.createDefault(token)
                .addEventListeners(waiter, client.build())
                .build()
                .awaitStatus(JDA.Status.CONNECTED)
                .awaitReady();
    }
}