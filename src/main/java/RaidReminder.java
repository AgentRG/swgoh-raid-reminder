import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.Start;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.log4j.BasicConfigurator;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RaidReminder {

    /**
     * Paths for the database and the config file from which Discord credentials will be read will need to be set before the bot can function.
     */

    public static void main (String[] args) throws IOException, LoginException, InterruptedException {

        List<String> configFile = Files.readAllLines(Paths.get("F:Database/config.txt")); //Set path for the config file containing Discord credentials
        BasicConfigurator.configure();
        var waiter = new EventWaiter();

        String token = configFile.get(0);
        String ownerId = configFile.get(1);

        var client = new CommandClientBuilder()
                .setActivity(Activity.listening("rr.help"))
                .setStatus(OnlineStatus.ONLINE)
                .setOwnerId(ownerId)
                .setPrefix("rr.")
                .addCommands(new Start());

        JDABuilder.createDefault(token)
                .addEventListeners(waiter, client.build())
                .build()
                .awaitStatus(JDA.Status.CONNECTED)
                .awaitReady();
    }
}
