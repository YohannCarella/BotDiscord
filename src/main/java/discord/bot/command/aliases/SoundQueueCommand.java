package discord.bot.command.aliases;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioTrackToTrackUtil;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.audio.Track;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

public class SoundQueueCommand extends ICommand {
    private final String HELP = "Display the current playlist status played. \nUsage : `!" + this.commandName + "`";
    private final String PLAYLIST_STATUS = "Playlist current status : ";
    private final String NO_MORE_SOUND = "No more track to be played.";
    private final String NO_SOUND_PLAYING = "No sound is currently being played.";
    private final String COMMAND_FAILED = "Failed displaying the playlist.";
    private final String EMPTY_QUEUE = "Playlist is empty.";
    private static Logger logger = Logger.getLogger(SoundQueueCommand.class);

    public SoundQueueCommand(String commandName){
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    private String getFormattedTrackName(Track trackToFormat){
        return trackToFormat.getTitle() + " - " + trackToFormat.getChannelTitle() + "\n";
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            GuildMusicManager musicManager = GuildMusicManagerSupervisor.getInstance().getGuildMusicManager(event.getGuild().getIdLong());
            String trackTitleList = EMPTY_QUEUE;
            String currentTrack = NO_SOUND_PLAYING + "\n";
            EmbedBuilder builder = new EmbedBuilder();
            if(musicManager.scheduler.getTrackAmount() > 0){
                ArrayList<AudioTrack> trackList = musicManager.scheduler.getTrackList();
                builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
                currentTrack = getFormattedTrackName(AudioTrackToTrackUtil.convert(trackList.get(0))) + "\n";
                trackTitleList = "";
                for(int i = 1; i < trackList.size(); i++){
                    trackTitleList += i + " - " + getFormattedTrackName(AudioTrackToTrackUtil.convert(trackList.get(i)));
                }
            }else {
                builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            }
            if(musicManager.scheduler.getTrackAmount() == 1){
                trackTitleList = NO_MORE_SOUND;
            }
            builder.setAuthor(PLAYLIST_STATUS);
            builder.setColor(Color.ORANGE);
            builder.addField("Current track :loud_sound:", currentTrack, false);
            builder.addField("Loop :repeat_one:", (ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(),PropertyEnum.LOOP.getPropertyName()).equals("true") ? "Enabled. \n" : "Disabled. \n"), false );
            builder.addField("Queued tracks :bulb:", trackTitleList, false);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),builder.build());
        }catch (Exception e){
            logger.log(Level.ERROR, event.getMessage(), e);
            MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),COMMAND_FAILED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
