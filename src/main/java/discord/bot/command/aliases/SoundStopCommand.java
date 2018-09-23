package discord.bot.command.aliases;

import discord.bot.command.ICommand;
import discord.bot.utils.audio.AudioServerManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Map;

public class SoundStopCommand extends ICommand {
    private final String HELP = "Stop the sound playing and disconnect the bot from the voice channel. \nUsage : `!" + this.commandName + "`";
    private final String SOUND_SKIPPED = "Sound has been stopped.";
    private final String COMMAND_FAILED = "Failed executing this command. Please make sure a track is being played.";

    private Map<String,AudioServerManager> audioServerManagers;

    public SoundStopCommand(Map<String,AudioServerManager> audioServerManagers, String commandName){
        super(commandName);
        this.audioServerManagers =  audioServerManagers;
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return (args.length == 0 || !args[0].equals("help")) && args.length == 0;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        try{
            AudioServerManager currAudioServerManager = audioServerManagers.get(event.getGuild().getId());
            currAudioServerManager.emptyPlaylist();
            currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().setSendingHandler(null);
            currAudioServerManager.getAudioLoadResultHandler().getGuildAudioManager().closeAudioConnection();
            event.getTextChannel().sendMessage(SOUND_SKIPPED).queue();
        }catch (Exception e){
            event.getTextChannel().sendMessage(COMMAND_FAILED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}
