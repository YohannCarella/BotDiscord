package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.GuildMusicManager;
import discord.bot.utils.audio.GuildMusicManagerSupervisor;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SoundShuffleCommand extends ICommand {
    private final String HELP = "Shuffles the playlist order. \nUsage : `!" + this.commandName + "`";
    private final String PLAYLIST_SHUFFLED = "Playlist has been successfully shuffled.";
    private final String NO_TRACK_TO_SHUFFLE = "Playlist don't have enough tracks to be shuffled :thinking:";
    private final String COMMAND_FAILED = "Failed shuffleing the playlist. Please make sure tracks are queued.";
    private static Logger logger = Logger.getLogger(SoundShuffleCommand.class);

    public SoundShuffleCommand(String commandName){
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        try{
            GuildMusicManager musicManager = GuildMusicManagerSupervisor.getInstance().getGuildMusicManager(event.getGuild().getIdLong());
            if(musicManager.scheduler.getTrackAmount() > 1){
                musicManager.scheduler.shuffle();
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),PLAYLIST_SHUFFLED);
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),NO_TRACK_TO_SHUFFLE);
            }
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
