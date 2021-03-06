package discord.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import discord.bot.listeners.GuildMovementListener;
import discord.bot.listeners.MessageListener;
import discord.bot.listeners.UserMovementListener;
import discord.bot.utils.misc.YoutubeApi;
import discord.bot.utils.save.PropertiesLoader;
import discord.bot.utils.save.SaveThread;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BotGlobalManager {
    private static List<JDA> shards;
    private static PropertiesLoader config = new PropertiesLoader();
    private static AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private static YoutubeApi youtubeApi = new YoutubeApi();
    private final int SHARD_AMMOUNT = 5;
    private static Logger logger = Logger.getLogger(BotGlobalManager.class);

    BotGlobalManager() {
        try {
            shards = new ArrayList<>();
            JDABuilder shardBuilder = new JDABuilder(AccountType.BOT).setActivity(Activity.of(Activity.ActivityType.WATCHING, "Service starting")).setToken(config.getBotToken()).setBulkDeleteSplittingEnabled(false);
            shardBuilder.addEventListeners(new MessageListener());
            shardBuilder.addEventListeners(new UserMovementListener());
            shardBuilder.addEventListeners(new GuildMovementListener());
            audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
            for (int i = 0; i < SHARD_AMMOUNT; i++) {
                shards.add(shardBuilder.useSharding(i, SHARD_AMMOUNT).build().awaitStatus(JDA.Status.CONNECTED));
                shards.get(i).getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "Service starting"));
            }
            config.initializeSavedProperties();
            SaveThread saveThread = new SaveThread();
            saveThread.start();
            serviceStartedNotification();
            logger.log(Level.INFO, "BOT started");
        } catch (LoginException | InterruptedException e) {
            logger.log(Level.ERROR, "Something went wrong", e);
            System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
        }
    }//Constructeur de la JDA permettant de faire fonctionner le bot

    private void serviceStartedNotification() {
        for (int i = 0; i < shards.size(); i++) {
            shards.get(i).getPresence().setActivity(Activity.watching("jacksonbot.com | !help"));
        }
    }

    public static void main(String[] args) {
        new BotGlobalManager();
    }//Fonction main

    public static YoutubeApi getYoutubeApi() {
        return youtubeApi;
    }

    public static PropertiesLoader getConfig() {
        return config;
    }

    public static AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public static List<Guild> getServers() {
        List<Guild> servers = new ArrayList<>();
        for (int i = 0; i < shards.size(); i++) {
            servers.addAll(shards.get(i).getGuilds());
        }
        return servers;
    }

    public static Guild getSupportServer() {
        for (int i = 0; i < shards.size(); i++) {
            if (shards.get(i).getGuildById(config.getSupportServerId()) != null) {
                return shards.get(i).getGuildById(config.getSupportServerId());
            }
        }
        return null;
    }

    public static TextChannel getBotLogsChannel(){
        return Objects.requireNonNull(getSupportServer()).getTextChannelById(config.getBotLogsChannelId());
    }

    public static List<JDA> getShards() {
        return shards;
    }
}
